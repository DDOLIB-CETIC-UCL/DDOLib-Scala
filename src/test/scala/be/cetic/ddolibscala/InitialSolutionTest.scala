package be.cetic.ddolibscala

import be.cetic.ddolibscala.common.solver.stopcriterion.StopCriterion
import be.cetic.ddolibscala.common.util.DebugMode
import be.cetic.ddolibscala.layered.modeling.SimpleDominanceChecker
import org.scalatest.funsuite.AnyFunSuite

import be.cetic.ddolibscala.examples.layered.{misp as layeredMisp}
import be.cetic.ddolibscala.examples.nolayer.{misp as noLayerMisp}
import be.cetic.ddolibscala.layered.{solver as layeredSolver}
import be.cetic.ddolibscala.nolayer.{solver as noLayerSolver}

class InitialSolutionTest extends AnyFunSuite {

  private val instance = "src/test/resources/MISP/C_6_weighted.dot"

  // ----- Layered -----

  private val layeredProblem = layeredMisp.MispProblem(instance)
  private val layeredFlb     = layeredMisp.MispFlb(layeredProblem)
  private val layeredOptimum = layeredProblem.optimal.get

  /** An optimal solution, rebuilt by hand from the assignment found by the exact solver. */
  private lazy val layeredOptimalSolution: layeredSolver.Solution = {
    val exact = Solvers.layered.exact(problem = layeredProblem, lowerBound = layeredFlb).minimize()
    layeredSolver.Solution(exact.solution(), exact.value())
  }

  /** Every layered solver taking an initial solution. */
  private val layeredSolvers
    : List[(String, Option[layeredSolver.Solution] => layeredSolver.Solution)] = List(
    "DDO" -> (init =>
      Solvers.layered
        .ddo(
          problem = layeredProblem,
          relaxation = layeredMisp.MispRelaxation(),
          lowerBound = layeredFlb,
          initialSolution = init,
          ranking = layeredMisp.MispRanking()
        )
        .minimize()
    ),
    "A*"      -> (init =>
      Solvers.layered
        .astar(problem = layeredProblem, lowerBound = layeredFlb, initialSolution = init)
        .minimize()
    ),
    "AWA*"    -> (init =>
      Solvers.layered
        .awastar(problem = layeredProblem, lowerBound = layeredFlb, initialSolution = init)
        .minimize()
    ),
    "ACS"     -> (init =>
      Solvers.layered
        .acs(problem = layeredProblem, lowerBound = layeredFlb, initialSolution = init)
        .minimize()
    ),
    // LNS cannot always prove optimality, hence the limit.
    "LNS" -> (init =>
      Solvers.layered
        .lns(
          problem = layeredProblem,
          lowerBound = layeredFlb,
          initialSolution = init,
          ranking = layeredMisp.MispRanking()
        )
        .minimize(limit = StopCriterion.maxIter(20))
    )
  )

  test("layered Solution built by hand exposes its assignment and value") {
    val solution = layeredSolver.Solution(Array(1, 0, 1), -4.0)

    assert(!solution.isEmpty)
    assert(solution.solution().sameElements(Array(1, 0, 1)))
    assert(solution.value() == -4.0)
  }

  test("layered Solution built from an empty assignment is empty") {
    assert(layeredSolver.Solution(Array.empty[Int], 0.0).isEmpty)
  }

  layeredSolvers.foreach { (name, solve) =>
    test(s"layered $name keeps the optimum given an optimal initial solution") {
      val solution = solve(Some(layeredOptimalSolution))

      assert(solution.value() == layeredOptimum)
      assert(layeredProblem.evaluate(solution.solution()) == layeredOptimum)
    }

    test(s"layered $name reaches the optimum from a DFS initial solution") {
      val initial  = StartHeuristics.layered.dfs(
        problem = layeredProblem,
        lowerBound = layeredFlb,
        dominance = SimpleDominanceChecker(layeredMisp.MispDominance(), layeredProblem.nbVars())
      )
      val solution = solve(Some(initial))

      assert(solution.value() == layeredOptimum)
      assert(layeredProblem.evaluate(solution.solution()) == layeredOptimum)
    }
  }

  test("layered solver rejects an initial solution with a wrong value in debug mode") {
    val wrong = layeredSolver.Solution(
      layeredOptimalSolution.solution(),
      layeredOptimalSolution.value() - 1
    )

    assertThrows[IllegalArgumentException] {
      Solvers.layered
        .astar(
          problem = layeredProblem,
          lowerBound = layeredFlb,
          initialSolution = Some(wrong),
          debugMode = DebugMode.On
        )
        .minimize()
    }
  }

  // ----- NoLayer -----

  private val noLayerProblem   = noLayerMisp.MispProblem(instance)
  private val noLayerFlb       = noLayerMisp.MispFlb(noLayerProblem)
  private def noLayerDominance = noLayerMisp.MispNoLayerDominance(noLayerProblem.numNodes)
  private val noLayerOptimum   = noLayerProblem.optimal.get

  /** An optimal solution, rebuilt by hand from the labels found by A*. */
  private lazy val noLayerOptimalSolution: noLayerSolver.Solution = {
    val astar = Solvers.nolayer.astar(problem = noLayerProblem, lowerBound = noLayerFlb).minimize()
    noLayerSolver.Solution(astar.solution(), astar.value())
  }

  /** Every nolayer solver taking an initial solution. */
  private val noLayerSolvers
    : List[(String, Option[noLayerSolver.Solution] => noLayerSolver.Solution)] = List(
    "A*"   -> (init =>
      Solvers.nolayer
        .astar(
          problem = noLayerProblem,
          lowerBound = noLayerFlb,
          initialSolution = init,
          dominance = noLayerDominance
        )
        .minimize()
    ),
    "AWA*" -> (init =>
      Solvers.nolayer
        .awastar(
          problem = noLayerProblem,
          lowerBound = noLayerFlb,
          initialSolution = init,
          dominance = noLayerDominance
        )
        .minimize()
    ),
    "ACS"  -> (init =>
      Solvers.nolayer
        .acs(
          problem = noLayerProblem,
          lowerBound = noLayerFlb,
          initialSolution = init,
          dominance = noLayerDominance
        )
        .minimize()
    )
  )

  test("nolayer Solution built by hand exposes its labels and value") {
    val solution = noLayerSolver.Solution(Seq(2, 0), -3.0)

    assert(!solution.isEmpty)
    assert(solution.solution() == Seq(2, 0))
    assert(solution.value() == -3.0)
  }

  test("nolayer Solution built from an empty sequence is empty") {
    assert(noLayerSolver.Solution(Seq.empty, 0.0).isEmpty)
  }

  noLayerSolvers.foreach { (name, solve) =>
    test(s"nolayer $name keeps the optimum given an optimal initial solution") {
      val solution = solve(Some(noLayerOptimalSolution))

      assert(solution.value() == noLayerOptimum)
      assert(noLayerProblem.evaluate(solution.solution()) == noLayerOptimum)
    }

    test(s"nolayer $name reaches the optimum from a DFS initial solution") {
      val initial  = StartHeuristics.nolayer.dfs(
        problem = noLayerProblem,
        lowerBound = noLayerFlb,
        dominance = noLayerDominance
      )
      val solution = solve(Some(initial))

      assert(solution.value() == noLayerOptimum)
      assert(noLayerProblem.evaluate(solution.solution()) == noLayerOptimum)
    }
  }

  test("nolayer solver rejects an initial solution with a wrong value in debug mode") {
    val wrong = noLayerSolver.Solution(
      noLayerOptimalSolution.solution(),
      noLayerOptimalSolution.value() - 1
    )

    assertThrows[IllegalArgumentException] {
      Solvers.nolayer
        .astar(
          problem = noLayerProblem,
          lowerBound = noLayerFlb,
          initialSolution = Some(wrong),
          debugMode = DebugMode.On
        )
        .minimize()
    }
  }
}
