package org.ddolibscala // to get access to the api
package example.misp

/* OR
package org.ddolibscala.example.misp
import org.ddolibscala.*
 */

import org.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.util.VerbosityLvl.LARGE

/** Example of MISP resolution with DDO Solver. */
object MispDdoMain {

  def main(args: Array[String]): Unit = {

    val problem        = MispProblem("data/MISP/50_nodes_1.dot")
    val solver: Solver =
      Solver.ddo(
        problem = problem,
        relaxation = MispRelaxation(),
        lowerBound = MispFlb(problem),
        widthHeuristic = FixedWidth(2),
        ranking = MispRanking(),
        frontier = Frontier,
        verbosityLvl = LARGE,
        useCache = true
      )

    val solution: Solution =
      solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
        println("------ NEW BEST ------")
        println(stats)
        println(sol.mkString("[", ", ", "]"))
      })

    println(solution)
    println(s"Search time: ${solution.statistics().runTimeMs()} ms")

  }

}
