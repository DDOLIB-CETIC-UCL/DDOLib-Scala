package org.ddolibscala
package example.misp

import org.ddolib.util.io.SolutionPrinter
import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.util.VerbosityLvl.LARGE

object MispDdoMain {

  def main(args: Array[String]): Unit = {

    val problem        = MispProblem("data/MISP/weighted.dot")
    val solver: Solver =
      DdoSolver(
        problem = problem,
        relaxation = MispRelaxation(),
        lowerBound = MispFlb(problem),
        widthHeuristic = FixedWidth(2),
        ranking = MispRanking(),
        frontier = Frontier,
        verbosityLvl = LARGE
      )

    val solution: Solution = solver.minimize(onSolution =
      (sol: Array[Int], stats: SearchStatistic) => SolutionPrinter.printSolution(stats, sol)
    )

    println(solution)
    println(s"Seach time: ${solution.statistics().runTimeMs()} ms")

  }

}
