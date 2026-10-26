package be.cetic.ddolibscala
package examples.nolayer.misp

import be.cetic.ddolibscala.nolayer.solver.{Solution, Solver}

object MispAstarMain {
  def main(args: Array[String]): Unit = {
    val problem: MispProblem = MispProblem("data/MISP/tadpole_4_2.dot")

    // A first solution found by a depth-first dive gives an initial upper bound to A*.
    val initial: Solution = StartHeuristics.nolayer.dfs(
      problem = problem,
      lowerBound = MispFlb(problem),
      dominance = MispNoLayerDominance(problem.numNodes)
    )
    println(s"DFS solution: $initial")

    val solver: Solver = Solvers.nolayer.astar(
      problem = problem,
      lowerBound = MispFlb(problem),
      initialSolution = Some(initial),
      dominance = MispNoLayerDominance(problem.numNodes)
    )

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Search time: ${solution.statistics().runtime()} ms")
    println(problem.evaluate(solution.solution()))
  }

}
