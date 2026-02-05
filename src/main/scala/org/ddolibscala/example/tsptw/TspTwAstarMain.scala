package org.ddolibscala
package example.tsptw

import org.ddolibscala.tools.dominance.SimpleDominanceChecker

import java.nio.file.Paths

/** Example of TSPTW resolution with an A* solver. */
object TspTwAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = TspTwProblem(Paths.get("data", "TSPTW", "AFG", "rbg010a.tw").toString)
    val solver  = Solver.astar(
      problem,
      lowerBound = TspTwFlb(problem),
      dominance = SimpleDominanceChecker(TspTwDominance(), problem.nbVars())
    )

    val solution = solver.minimize(onSolution = (sol, stats) => {
      println("------ NEW BEST ------")
      println(stats)
      println(s"0 -> ${sol.mkString(" -> ")}")
    })

    println("\n------ LAST SOLUTION FOUND ------")
    println(s"0 -> ${solution.solution().mkString(" -> ")}")
    println(s"Value: ${solution.value()}")
    println(s"Search time: ${solution.statistics().runTimeMs()} ms")

  }

}
