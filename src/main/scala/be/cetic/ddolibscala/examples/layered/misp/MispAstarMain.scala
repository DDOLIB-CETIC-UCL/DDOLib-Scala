package be.cetic.ddolibscala
package examples.layered.misp

import be.cetic.ddolibscala.layered.solver.{Solution, Solver}


/** Example of MISP resolution with A* solver */
object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")

    val solver: Solver = Solvers.layered.astar(problem = problem, lowerBound = MispFlb(problem))

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Search time: ${solution.statistics().runtime()} ms")
  }

}
