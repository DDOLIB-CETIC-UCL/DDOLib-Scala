package be.cetic.ddolibscala
package example.tsptw.layered

import be.cetic.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import be.cetic.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import be.cetic.ddolibscala.tools.dominance.SimpleDominanceChecker
import be.cetic.ddolibscala.util.DebugMode.On

import java.nio.file.Paths

/** Example of TSPTW resolution with a DDO solver. */
object TspTwDdoMain {

  def main(args: Array[String]): Unit = {
    val problem = TspTwProblem(Paths.get("data", "TSPTW", "AFG", "rbg010a.tw").toString)
    val solver  = Solver.ddo(
      problem,
      relaxation = TspTwRelax(problem.nbVars()),
      lowerBound = TspTwFlb(problem),
      dominance = SimpleDominanceChecker(TspTwDominance(), problem.nbVars()),
      ranking = TspTwRanking(),
      widthHeuristic = FixedWidth(100),
      frontier = Frontier,
      useCache = true,
      debugMode = On
    )

    val solution = solver.minimize(onSolution = (sol, stats) => {
      println("------ NEW BEST ------")
      println(stats)
      println(s"0 -> ${sol.mkString(" -> ")}")
    })

    println("\n------ LAST SOLUTION FOUND ------")
    println(s"0 -> ${solution.solution().mkString(" -> ")}")
    println(s"Value: ${solution.value()}")
    println(s"Search time: ${solution.statistics().runtime()} ms")
  }

}
