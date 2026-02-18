package org.ddolibscala
package util.testbench

import org.ddolibscala.modeling.*
import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.DebugMode.On

import scala.collection.mutable.ListBuffer

/** Companion object of the [[ProblemTestBench]] class. */
object ProblemTestBench {

  /** Returns a generator of test cases for [[org.ddolibscala.modeling.Problem]] implementations.
    *
    * @param problems
    *   the list of problem instances to test.
    * @param configFactory
    *   a function providing the solver configuration for a given problem.
    * @tparam S
    *   the type of the state in the problem.
    * @tparam P
    *   the type of the problem implementation.
    * @return
    *   a generator of test cases for [[org.ddolibscala.modeling.Problem]] implementations.
    */
  def apply[S, P <: Problem[S]](
    problems: List[P],
    configFactory: P => TestModel[S]
  ): ProblemTestBench[S, P] = new ProblemTestBench(problems, configFactory)
}

/** A standalone generator of test cases for [[org.ddolibscala.modeling.Problem]] implementations.
  *
  * Instead of being a test suite itself, it generates a list of executable [[GeneratedTest]]
  * objects that can be run by any test framework.
  *
  * @param problems
  *   the list of problem instances to test.
  * @param configFactory
  *   a function providing the solver configuration for a given problem.
  * @tparam S
  *   the type of the state in the problem.
  * @tparam P
  *   the type of the problem implementation.
  */
class ProblemTestBench[S, P <: Problem[S]](problems: List[P], configFactory: P => TestModel[S]) {

  /** The minimal width of the MDD to test. */
  var minWidth: Int = 2

  /** The maximal width of the MDD to tests. */
  var maxWidth: Int = 20

  /** Generates all the test cases based on the provided problems and configuration.
    *
    * @return
    *   a list of test cases ready to be executed.
    */
  def generateTests(): List[GeneratedTest] = {
    problems.flatMap { p =>
      val config = configFactory(p)
      val tests  = ListBuffer[GeneratedTest]()

      // Helper to add a test
      def add(name: String)(body: => Unit): Unit = {
        tests += GeneratedTest(s"$name for $p", () => body)
      }

      add("Transition Model") {
        val solver = Solver.exact(p, debugMode = On)
        assertSolution(solver.minimize(), p)
      }

      if (!config.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
        add("FLB") {
          val solver = Solver.exact(p, lowerBound = config.flb, debugMode = On)
          assertSolution(solver.minimize(), p)
        }
      }

      if (!config.dominance.isInstanceOf[DefaultDominanceChecker[?]]) {
        add("Dominance") {
          val solver = Solver.exact(p, dominance = config.dominance, debugMode = On)
          assertSolution(solver.minimize(), p)
        }
      }

      config.relaxation.foreach { relax =>
        add("Relaxation") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              p,
              relaxation = relax,
              widthHeuristic = FixedWidth(w),
              ranking = config.ranking,
              debugMode = On
            )
            assertSolution(solver.minimize(), p, width = w)
          }
        }

        if (!config.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
          add("Relax & Flb") {
            for (w <- minWidth to maxWidth) {
              val solver = Solver.ddo(
                p,
                relaxation = relax,
                lowerBound = config.flb,
                widthHeuristic = FixedWidth(w),
                ranking = config.ranking,
                debugMode = On
              )
              assertSolution(solver.minimize(), p, width = w)
            }
          }
        }

        add("Cache") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              p,
              relaxation = relax,
              lowerBound = config.flb,
              dominance = config.dominance,
              ranking = config.ranking,
              widthHeuristic = FixedWidth(w),
              frontier = Frontier,
              useCache = true,
              debugMode = On
            )
            assertSolution(solver.minimize(), p, width = w)
          }
        }
      }

      add("A*") {
        val solver =
          Solver.astar(p, lowerBound = config.flb, dominance = config.dominance, debugMode = On)
        assertSolution(solver.minimize(), p)
      }

      add("ACS") {
        val solver =
          Solver.acs(p, lowerBound = config.flb, dominance = config.dominance, debugMode = On)
        assertSolution(solver.minimize(), p)
      }

      tests.toList
    }
  }

  /** Internal assertion logic using standard Scala asserts to remain framework-agnostic.
    */
  private def assertSolution(bestSolution: Solution, problem: P, width: Int = -1): Unit = {
    val bestValue       = bestSolution.value()
    val optBestVal      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal = problem.optimal

    val contextMsg = if (width != -1) s" [Max width: $width]" else ""

    // Helper for double comparison with tolerance
    def isEqual(a: Double, b: Double): Boolean = Math.abs(a - b) < 1e-10

    (optBestVal, expectedOptimal) match {
      case (Some(v), Some(e)) =>
        assert(isEqual(v, e), s"Expected $e but got $v$contextMsg")
      case (v, e) =>
        assert(v == e, s"Expected $e but got $v$contextMsg")
    }

    if (expectedOptimal.isDefined) {
      val calculatedVal = problem.evaluate(bestSolution.solution())
      assert(
        isEqual(calculatedVal, expectedOptimal.get),
        s"Solution evaluation failed. Expected ${expectedOptimal.get}, got $calculatedVal$contextMsg"
      )
    }
  }
}
