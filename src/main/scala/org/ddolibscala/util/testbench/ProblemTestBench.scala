package org.ddolibscala
package util.testbench

import org.ddolibscala.modeling.*
import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.{Try, Using}

/** Abstract base class to generate a comprehensive test suite for implementations of
  * [[org.ddolibscala.modeling.Problem]].
  *
  * This class leverages ScalaTest's `AnyFunSuite` to automatically generate tests for:
  *   - Exact solver resolution (transition model verification)
  *   - Fast Lower Bound (FLB) correctness
  *   - Dominance checking
  *   - Relaxation (Bounds and logic)
  *   - Caching mechanisms
  *   - A* and ACS solvers
  *
  * Users must extend this class and implement [[generateProblem]] and [[model]] to provide the
  * necessary data and configurations.
  *
  * @tparam T
  *   the type of the state in the problem
  * @tparam P
  *   the type of the problem implementation, subtype of [[org.ddolibscala.modeling.Problem]]
  */
abstract class ProblemTestBench[T, P <: Problem[T]] extends AnyFunSuite with Matchers {

  /** Defines the minimum width of the MDD to be used during relaxation tests. Defaults to 2.
    */
  protected def minWidth: Int = 2

  /** Defines the maximum width of the MDD to be used during relaxation tests. Defaults to 20.
    */
  protected def maxWidth: Int = 20

  /** Generates or loads the list of problem instances to be tested.
    *
    * @return
    *   a list of problem instances
    */
  protected def generateProblem(): List[P]

  /** Provides the solver configuration (test model components) for a specific problem instance.
    * This includes the FLB, Relaxation, Ranking, and Dominance strategies to be tested.
    *
    * @param problem
    *   the problem instance for which the configuration is required
    * @return
    *   a model containing the components to test
    */
  protected def model(problem: P): TestModel[T]

  /** Utility method to load problem instances from files located in a specific directory.
    *
    * @param pathToDir
    *   the string path to the directory containing the problem files
    * @param factory
    *   a function that creates a problem instance from a file path string
    * @return
    *   a list of loaded problem instances
    */
  protected def loadProblemsFromDir(pathToDir: String)(factory: String => P): List[P] = {
    val path = Paths.get(pathToDir)
    if (Files.exists(path)) {
      val allFiles: Try[List[String]] = Using(Files.list(path)) { stream =>
        stream.filter(Files.isRegularFile(_)).map(_.toString).toList.asScala.toList
      }
      allFiles.getOrElse(Nil).map(factory)
    } else {
      Nil
    }
  }

  /** Asserts that the solution found by the solver matches the expected optimal value defined in
    * the problem instance.
    *
    * It handles floating-point comparisons with a tolerance (epsilon) and verifies that the
    * solution path actually evaluates to the claimed value.
    *
    * @param bestSolution
    *   the solution returned by the solver
    * @param problem
    *   the problem instance containing the oracle (optimal value)
    * @param width
    *   the maximum width used during the test (for reporting purposes)
    */
  private def assertSolution(bestSolution: Solution, problem: P, width: Int = -1): Unit = {
    val bestValue                       = bestSolution.value()
    val optBestVal: Option[Double]      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal: Option[Double] = problem.optimal
    val clueMsg: String                 = if (width != -1) s"Max width of MDD: $width" else ""

    withClue(clueMsg) {
      if (optBestVal.isDefined && expectedOptimal.isDefined) {
        optBestVal.get must be(expectedOptimal.get +- 1e-10)
      } else {
        optBestVal must equal(expectedOptimal)
      }

      if (expectedOptimal.isDefined) {
        problem.evaluate(bestSolution.solution()) must be(expectedOptimal.get +- 1e-10)
      }
    }
  }

  private def registerTestsForProblem(p: P): Unit = {
    val m: TestModel[T]                              = model(p)
    def check(name: String)(testBody: => Unit): Unit = test(s"$name for $p") { testBody }

    check("Transition Model") {
      val solver = Solver.exact(p)
      assertSolution(solver.minimize(), p)
    }

    if (!m.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
      check("FLB") {
        val solver = Solver.exact(p, lowerBound = m.flb)
        assertSolution(solver.minimize(), p)
      }
    }

    if (!m.dominance.isInstanceOf[DefaultDominanceChecker[?]]) {
      check("Dominance") {
        val solver = Solver.exact(p, dominance = m.dominance)
        assertSolution(solver.minimize(), p)
      }
    }

    m.relaxation.foreach { relax =>
      check("Relaxation") {
        for (w <- minWidth to maxWidth) {
          val solver =
            Solver.ddo(p, relaxation = relax, widthHeuristic = FixedWidth(w), ranking = m.ranking)
          assertSolution(solver.minimize(), p, width = w)
        }
      }

      if (!m.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
        check("Relax & Flb") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              p,
              relaxation = relax,
              lowerBound = m.flb,
              widthHeuristic = FixedWidth(w),
              ranking = m.ranking
            )
            assertSolution(solver.minimize(), p, width = w)
          }
        }
      }

      check("Cache") {
        for (w <- minWidth to maxWidth) {
          val solver = Solver.ddo(
            p,
            relaxation = relax,
            lowerBound = m.flb,
            dominance = m.dominance,
            ranking = m.ranking,
            widthHeuristic = FixedWidth(w),
            frontier = Frontier,
            useCache = true
          )
          assertSolution(solver.minimize(), p, width = w)
        }
      }
    }

    check("A*") {
      val solver = Solver.astar(p, lowerBound = m.flb, dominance = m.dominance)
      assertSolution(solver.minimize(), p)
    }

    check("ACS") {
      val solver = Solver.acs(p, lowerBound = m.flb, dominance = m.dominance)
      assertSolution(solver.minimize(), p)
    }
  }

  generateProblem().foreach(registerTestsForProblem)
}
