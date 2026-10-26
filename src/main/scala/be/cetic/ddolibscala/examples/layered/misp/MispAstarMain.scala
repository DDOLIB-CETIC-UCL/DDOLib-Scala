package be.cetic.ddolibscala
package examples.layered.misp

import be.cetic.ddolibscala.layered.solver.{Solution, Solver}


/** Example of MISP resolution with A* solver */
object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")

    // A first solution found by a depth-first dive gives an initial upper bound to A*.
    val initial: Solution =
      StartHeuristics.layered.dfs(problem = problem, lowerBound = MispFlb(problem))
    println(s"DFS solution: $initial")

    val solver: Solver = Solvers.layered.astar(
      problem = problem,
      lowerBound = MispFlb(problem),
      initialSolution = Some(initial)
    )

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Search time: ${solution.statistics().runtime()} ms")
  }

}
