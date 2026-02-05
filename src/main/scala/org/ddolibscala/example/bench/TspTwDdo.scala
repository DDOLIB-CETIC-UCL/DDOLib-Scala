package org.ddolibscala
package example.bench

import org.ddolibscala.example.tsptw.*
import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.SimpleDominanceChecker

import java.nio.file.Paths

object TspTwDdo {

  def main(args: Array[String]): Unit = {
    val instance: String = {
      if (args.length == 0) Paths.get("data", "TSPTW", "AFG", "rbg010a.tw").toString
      else args(0)
    }

    val timeout: Long = if (args.length == 2) args(1).toLong else 100L
    val problem       = TspTwProblem(instance)
    val solver        = Solver.ddo(
      problem,
      relaxation = TspTwRelax(problem.nbVars()),
      lowerBound = TspTwFlb(problem),
      dominance = SimpleDominanceChecker(TspTwDominance(), problem.nbVars()),
      ranking = TspTwRanking(),
      widthHeuristic = FixedWidth(20),
      frontier = Frontier,
      useCache = true
    )

    val bestSolution = solver.minimize(
      limit = _.runTimeMs() > timeout,
      onSolution = (sol, stats) => {
        println(s"%%incumbent: ${stats.incumbent()} gap: ${stats.gap()} time:${stats.runTimeMs()}")
      }
    )

    val bestStats = bestSolution.statistics()

    println(
      s"%%optimality:${bestStats.status()} gap:${bestStats.gap()} time:${bestStats.runTimeMs()}"
    )

  }

}
