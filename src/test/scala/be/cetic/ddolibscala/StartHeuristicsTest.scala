package be.cetic.ddolibscala

import be.cetic.ddolibscala.common.solver.stopcriterion.StopCriterion
import be.cetic.ddolibscala.layered.modeling.SimpleDominanceChecker
import be.cetic.ddolibscala.util.testbench.ProblemLoader
import org.ddolib.common.solver.stat.SearchStatus
import org.scalatest.funsuite.AnyFunSuite

import be.cetic.ddolibscala.examples.layered.{misp as layeredMisp, tsptw}
import be.cetic.ddolibscala.examples.nolayer.{misp as noLayerMisp}

class StartHeuristicsTest extends AnyFunSuite {

  private val layeredProblems: List[layeredMisp.MispProblem] =
    ProblemLoader.loadFromDir("src/test/resources/MISP")(layeredMisp.MispProblem(_))

  private val noLayerProblems: List[noLayerMisp.MispProblem] =
    ProblemLoader.loadFromDir("src/test/resources/MISP")(noLayerMisp.MispProblem(_))

  private def layeredDfs(problem: layeredMisp.MispProblem) = {
    StartHeuristics.layered.dfs(
      problem = problem,
      lowerBound = layeredMisp.MispFlb(problem),
      dominance = SimpleDominanceChecker(layeredMisp.MispDominance(), problem.nbVars())
    )
  }

  private def noLayerDfs(problem: noLayerMisp.MispProblem) = {
    StartHeuristics.nolayer.dfs(
      problem = problem,
      lowerBound = noLayerMisp.MispFlb(problem),
      dominance = noLayerMisp.MispNoLayerDominance(problem.numNodes)
    )
  }

  layeredProblems.foreach { problem =>
    test(s"layered DFS finds a feasible solution for $problem") {
      val solution = layeredDfs(problem)

      assert(!solution.isEmpty)
      assert(solution.statistics().status() == SearchStatus.SAT)
      assert(solution.solution().length == problem.nbVars())
      assert(solution.value() == problem.evaluate(solution.solution()))
      problem.optimal.foreach(opt => assert(solution.value() >= opt))
    }
  }

  noLayerProblems.foreach { problem =>
    test(s"nolayer DFS finds a feasible solution for $problem") {
      val solution = noLayerDfs(problem)

      assert(!solution.isEmpty)
      assert(solution.statistics().status() == SearchStatus.SAT)
      assert(solution.value() == problem.evaluate(solution.solution()))
      problem.optimal.foreach(opt => assert(solution.value() >= opt))
    }
  }

  test("layered DFS returns an empty solution when the limit is reached") {
    val problem  = layeredProblems.head
    val solution = StartHeuristics.layered.dfs(
      problem = problem,
      lowerBound = layeredMisp.MispFlb(problem),
      limit = StopCriterion.maxIter(1)
    )

    assert(solution.isEmpty)
    assert(solution.statistics().status() == SearchStatus.UNKNOWN)
  }

  test("nolayer DFS returns an empty solution when the limit is reached") {
    val problem  = noLayerProblems.head
    val solution = StartHeuristics.nolayer.dfs(
      problem = problem,
      lowerBound = noLayerMisp.MispFlb(problem),
      limit = StopCriterion.maxIter(1)
    )

    assert(solution.isEmpty)
    assert(solution.statistics().status() == SearchStatus.UNKNOWN)
  }

  test("layered DFS returns an empty solution on an infeasible problem") {
    val problem  = tsptw.TspTwProblem("src/test/resources/TSPTW/impossible.txt")
    val solution = StartHeuristics.layered.dfs(
      problem = problem,
      lowerBound = tsptw.TspTwFlb(problem)
    )

    assert(solution.isEmpty)
    assert(solution.statistics().status() == SearchStatus.UNSAT)
  }
}
