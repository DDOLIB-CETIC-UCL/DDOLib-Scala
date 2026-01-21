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

abstract class ProblemTestBench[T, P <: Problem[T]] extends AnyFunSuite with Matchers {

  protected def minWidth: Int = 2
  protected def maxWidth: Int = 20

  protected def generateProblem(): List[P]

  protected def solverConfig(problem: P): SolverTestConfig[T]

  protected def loadProblemsFromDir(pathToDir: String)(factory: String => P): List[P] = {
    val path                        = Paths.get(pathToDir)
    val allFiles: Try[List[String]] = Using(Files.list(path)) { stream =>
      stream.filter(Files.isRegularFile(_)).map(_.toString).toList.asScala.toList
    }

    allFiles.getOrElse(Nil).map(factory)
  }

  private def assertSolution(bestSolution: Solution, problem: P, width: Int = -1): Unit = {
    val bestValue                       = bestSolution.value()
    val optBestVal: Option[Double]      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal: Option[Double] = problem.optimal
    val clueMsg: String                 = if (width != -1) s"Max width of MDD: $width" else ""

    withClue(clueMsg) {

      if (optBestVal.isDefined && expectedOptimal.isDefined) {
        optBestVal.get must ===(expectedOptimal.get +- 1e-10)
      } else {
        optBestVal must equal(expectedOptimal)
      }
      if (expectedOptimal.isDefined) {
        problem.evaluate(bestSolution.solution()) must ===(expectedOptimal.get +- 1e-10)
      }

    }
  }

  private def generateTests(problem: P): Unit = {
    val config = solverConfig(problem)

    def check(name: String)(testBody: => Unit): Unit = test(s"$name for $problem") { testBody }

    check("Transition Model") {
      val solver = Solver.exact(problem)
      assertSolution(solver.minimize(), problem)
    }

    if (!config.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
      check("FLB") {
        val solver = Solver.exact(problem, lowerBound = config.flb)
        assertSolution(solver.minimize(), problem)
      }
    }

    if (!config.dominance.isInstanceOf[DefaultDominanceChecker[?]]) {
      check("Dominance") {
        val solver = Solver.exact(problem, dominance = config.dominance)
        assertSolution(solver.minimize(), problem)
      }
    }

    config.relaxation.foreach { relax =>
      check("Relaxation") {
        for (w <- minWidth to maxWidth) {
          val solver =
            Solver.ddo(
              problem,
              relaxation = relax,
              widthHeuristic = FixedWidth(w),
              ranking = config.ranking
            )
          assertSolution(solver.minimize(), problem, width = w)
        }
      }

      if (!config.flb.isInstanceOf[DefaultFastLowerBound[?]]) {
        check("Relax & Flb") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              problem,
              relaxation = relax,
              lowerBound = config.flb,
              widthHeuristic = FixedWidth(w),
              ranking = config.ranking
            )
            assertSolution(solver.minimize(), problem, width = w)
          }
        }
      }

      check("Cache") {
        for (w <- minWidth to maxWidth) {
          val solver = Solver.ddo(
            problem,
            relaxation = relax,
            lowerBound = config.flb,
            dominance = config.dominance,
            ranking = config.ranking,
            widthHeuristic = FixedWidth(w),
            frontier = Frontier,
            useCache = true
          )
          assertSolution(solver.minimize(), problem, width = w)
        }
      }

    }

    check("A*") {
      val m      = solverConfig(problem)
      val solver = Solver.astar(problem, lowerBound = m.flb, dominance = m.dominance)
    }

    check("ACS") {
      val m      = solverConfig(problem)
      val solver = Solver.acs(problem, lowerBound = m.flb, dominance = m.dominance)
    }

  }

  generateProblem().foreach(generateTests)
}
