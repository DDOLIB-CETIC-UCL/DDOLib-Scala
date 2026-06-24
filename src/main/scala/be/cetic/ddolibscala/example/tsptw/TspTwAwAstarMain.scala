package be.cetic.ddolibscala.example.tsptw

import be.cetic.ddolibscala.solver.Solver
import be.cetic.ddolibscala.tools.dominance.SimpleDominanceChecker

import java.nio.file.Paths

object TspTwAwAstarMain {

  def main(args: Array[String]): Unit = {
    val problem = TspTwProblem(Paths.get("data", "TSPTW", "AFG", "rbg172a.tw").toString)
    val solver  = Solver.awastar(
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
