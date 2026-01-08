package org.ddolibscala
package example.misp

object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")

    val solver: Solver = AstarSolver(problem = problem, lowerBound = MispFlb(problem))

    val solution: Solution = solver.minimize()
    println(solution)
    println(s"Seach time: ${solution.statistics().runTimeMs()} ms")
  }

}
