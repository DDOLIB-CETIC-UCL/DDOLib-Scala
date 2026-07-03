package be.cetic.ddolibscala.examples.layered.misp


import be.cetic.ddolibscala.Solvers
import be.cetic.ddolibscala.common.frontier.CutSetType.Frontier
import be.cetic.ddolibscala.common.heuristics.width.FixedWidth
import be.cetic.ddolibscala.common.util.VerbosityLvl.Large
import be.cetic.ddolibscala.SearchStatistic
import be.cetic.ddolibscala.layered.solver.{Solution, Solver}

/** Example of MISP resolution with DDO Solver. */
object MispDdoMain {

  def main(args: Array[String]): Unit = {

    val problem        = MispProblem("data/MISP/50_nodes_1.dot")
    val solver: Solver =
      Solvers.layered.ddo(
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
