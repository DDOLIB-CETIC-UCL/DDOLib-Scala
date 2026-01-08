package org.ddolibscala // to get access to the API
package example.misp

/* OR
package org.ddolibscala.example.misp
import org.ddoliscala.*
 */

/** Example of MISP resolution with A* solver */
object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")

    val solver: Solver = AstarSolver(problem = problem, lowerBound = MispFlb(problem))

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Search time: ${solution.statistics().runTimeMs()} ms")
  }

}
