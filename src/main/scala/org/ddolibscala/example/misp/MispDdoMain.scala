package org.ddolibscala
package example.misp

import org.ddolib.util.io.SolutionPrinter
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth

object MispDdoMain {

  def main(args: Array[String]): Unit = {

    val problem = MispProblem("data/MISP/weighted.dot")
    val solver: Solver =
      DdoSolver(
        problem = problem,
        relaxation = MispRelaxation(),
        lowerBound = MispFlb(problem),
        widthHeuristic = FixedWidth(2),
        ranking = MispRanking()
      )

    val solution: Solution = solver.minimize(onSolution =
      (sol: Array[Int], stats: SearchStatistic) => SolutionPrinter.printSolution(stats, sol)
    )

    println(solution)
    println(s"Seach time: ${solution.statistics().runTimeMs()} ms")

  }

}
