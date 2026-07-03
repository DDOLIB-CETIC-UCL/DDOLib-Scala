package be.cetic.ddolibscala
package examples.layered.tsptw

import be.cetic.ddolibscala.layered.modeling.SimpleDominanceChecker

import java.nio.file.Paths

object TspTwAwAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = TspTwProblem(Paths.get("data", "TSPTW", "AFG", "rbg172a.tw").toString)
    val solver  = Solvers.layered.awastar(
      problem,
      weight = 7.5,
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
    println(solution.statistics())
  }

}
