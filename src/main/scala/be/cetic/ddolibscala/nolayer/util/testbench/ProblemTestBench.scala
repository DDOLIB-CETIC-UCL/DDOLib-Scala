package be.cetic.ddolibscala.nolayer.util.testbench

import be.cetic.ddolibscala.nolayer.solver.Solution
import be.cetic.ddolibscala.nolayer.modeling.Problem
import be.cetic.ddolibscala.Solvers
import be.cetic.ddolibscala.common.util.DebugMode.On
import be.cetic.ddolibscala.util.testbench.GeneratedTest

import scala.collection.mutable.ListBuffer

/** A standalone generator of test cases for NoLayer [[Problem]] implementations.
  *
  * It generates a list of executable [[GeneratedTest]] objects that can be run by any test
  * framework.
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
class ProblemTestBench[S, P <: Problem[S]](
  problems: List[P],
  configFactory: P => TestModel[S],
  bestSolutionKnown: Boolean
) {

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

      add("A*") {
        val solver = Solvers.nolayer.astar(
          problem = p,
          lowerBound = config.flb,
          dominance = config.dominance,
          debugMode = On
        )
        assertSolution(solver.minimize(), p)
      }

      add("ACS") {
        val solver = Solvers.nolayer.acs(
          problem = p,
          lowerBound = config.flb,
          dominance = config.dominance,
          debugMode = On
        )
        assertSolution(solver.minimize(), p)
      }

      add("AWA*") {
        val solver = Solvers.nolayer.awastar(
          problem = p,
          lowerBound = config.flb,
          dominance = config.dominance,
          debugMode = On
        )
        assertSolution(solver.minimize(), p)
      }

      tests.toList
    }
  }

  /** Internal assertion logic using standard Scala asserts to remain framework-agnostic.
    */
  private def assertSolution(bestSolution: Solution, problem: P): Unit = {
    val bestValue       = bestSolution.value()
    val optBestVal      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal = problem.optimal

    // Helper for double comparison with tolerance
    def isEqual(a: Double, b: Double): Boolean = Math.abs(a - b) < 1e-10

    (optBestVal, expectedOptimal) match {
      case (Some(v), Some(e)) =>
        assert(isEqual(v, e), s"Expected $e but got $v")
      case (v, e) if bestSolutionKnown =>
        assert(v == e, s"Expected $e but got $v")
      case _ => ;
    }

    if (expectedOptimal.isDefined) {
      val calculatedVal = problem.evaluate(bestSolution.solution())
      assert(
        isEqual(calculatedVal, expectedOptimal.get),
        s"Solution evaluation failed. Expected ${expectedOptimal.get}, got $calculatedVal"
      )
    }
  }
}
