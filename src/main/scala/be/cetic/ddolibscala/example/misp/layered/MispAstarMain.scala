package be.cetic.ddolibscala
package example.misp.layered

// to get access to the API
/* OR
package org.ddolibscala.example.misp
import org.ddolibscala.*
 */

/** Example of MISP resolution with A* solver */
object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")

    val solver: Solver = Solver.layered.astar(problem = problem, lowerBound = MispFlb(problem))

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Search time: ${solution.statistics().runtime()} ms")
  }

}
