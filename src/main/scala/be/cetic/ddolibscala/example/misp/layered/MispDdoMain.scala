package be.cetic.ddolibscala.example.misp.layered

// to get access to the api

/* OR
package org.ddolibscala.example.misp
import org.ddolibscala.*
 */

import be.cetic.ddolibscala.solver.Solver
import be.cetic.ddolibscala.tools.ddo.frontier.CutSetType.Frontier
import be.cetic.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import be.cetic.ddolibscala.util.VerbosityLvl.Large
import be.cetic.ddolibscala.{SearchStatistic, Solution}

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
        verbosityLvl = Large,
        useCache = true
      )

    val solution: Solution =
      solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
        println("------ NEW BEST ------")
        println(stats)
        println(sol.mkString("[", ", ", "]"))
      })

    println(solution)
    println(s"Search time: ${solution.statistics().runtime()} ms")
  }

}
