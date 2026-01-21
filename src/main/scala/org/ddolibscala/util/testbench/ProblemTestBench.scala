package org.ddolibscala
package util.testbench

import org.ddolibscala.modeling.*
import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

abstract class ProblemTestBench[T, P <: Problem[T]]() extends AnyFunSuite with Matchers {

  private val problems: List[P] = generateProblem()

  protected var minWidth: Int = 2
  protected var maxWidth: Int = 20

  protected def generateProblem(): List[P]

  protected def model(problem: P): TestModel[T, P]

  private def testSolution(bestSolution: Solution, problem: P, width: Int = -1): Unit = {
    val bestValue                       = bestSolution.value()
    val optBestVal: Option[Double]      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal: Option[Double] = problem.optimal
    withClue({ if (width != -1) s"Max width of MDD: $width" else "" }) {
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

  private def testTransitionModel(problem: P): Unit = {
    val m              = model(problem)
    val solver: Solver = Solver.exact(problem)

    test(s"Model for $problem") {
      val sol = solver.minimize()
      testSolution(sol, problem)
    }
  }

  private def testFlb(problem: P): Unit = {
    val m = model(problem)

    m.flb match {
      case _: DefaultFastLowerBound[T] => ()
      case _                           =>
        val solver = Solver.exact(problem, lowerBound = m.flb)
        test(s"FLB for $problem") {
          testSolution(solver.minimize(), problem)
        }
    }
  }

  private def testDominance(problem: P): Unit = {
    val m = model(problem)

    m.dominance match {
      case _: DefaultDominanceChecker[T] => ()
      case _                             =>
        val solver = Solver.exact(problem, dominance = m.dominance)
        test(s"Dominance for $problem") {
          testSolution(solver.minimize(), problem)
        }
    }
  }

  private def testRelaxation(problem: P): Unit = {
    val m = model(problem)
    m.relaxation match {
      case None        => ()
      case Some(relax) =>
        test(s"Relaxation for $problem") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              problem,
              relaxation = relax,
              widthHeuristic = FixedWidth[T](w),
              ranking = m.ranking
            )
            testSolution(solver.minimize(), problem, width = w)
          }
        }
    }
  }

  private def testFlbOnRelaxation(problem: P): Unit = {
    val m = model(problem)
    (m.relaxation, m.flb) match {
      case (Some(relax), d) if !d.isInstanceOf[DefaultFastLowerBound[T]] =>
        test(s"Relax and Flb for $problem") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              problem,
              relaxation = relax,
              lowerBound = d,
              widthHeuristic = FixedWidth(w),
              ranking = m.ranking
            )
            testSolution(solver.minimize(), problem, width = w)
          }
        }
      case _ => ()
    }
  }

  private def testCache(problem: P): Unit = {
    val m = model(problem)

    m.relaxation match {
      case None        => ()
      case Some(relax) =>
        test(s"Cache for $problem") {
          for (w <- minWidth to maxWidth) {
            val solver = Solver.ddo(
              problem,
              relaxation = relax,
              lowerBound = m.flb,
              dominance = m.dominance,
              ranking = m.ranking,
              widthHeuristic = FixedWidth(w),
              frontier = Frontier,
              useCache = true
            )

            testSolution(solver.minimize(), problem, width = w)
          }
        }
    }
  }

  private def testAstar(problem: P): Unit = {
    val m      = model(problem)
    val solver = Solver.astar(problem, lowerBound = m.flb, dominance = m.dominance)
    test(s"A* for $problem") {
      testSolution(solver.minimize(), problem)
    }
  }

  private def testAcs(problem: P): Unit = {
    val m      = model(problem)
    val solver = Solver.acs(problem, lowerBound = m.flb, dominance = m.dominance)
    test(s"ACS for $problem") {
      testSolution(solver.minimize(), problem)
    }
  }

  for (p <- problems) {
    testTransitionModel(p)
    testFlb(p)
    testDominance(p)
    testRelaxation(p)
    testFlbOnRelaxation(p)
    testCache(p)
    testAstar(p)
    testAcs(p)
  }
}
