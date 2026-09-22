package be.cetic.ddolibscala.examples.layered.knapsack

import be.cetic.ddolibscala.Solvers
import be.cetic.ddolibscala.common.frontier.CutSetType.Frontier
import be.cetic.ddolibscala.common.heuristics.width.FixedWidth
import be.cetic.ddolibscala.common.util.VerbosityLvl.Large
import be.cetic.ddolibscala.SearchStatistic
import be.cetic.ddolibscala.layered.solver.{Solution, Solver}

/** Example of KnapSack resolution with DDO Solver. */
object KnapSachDdoMain {
  def main(args: Array[String]): Unit = {
    //val capa = 8000
    //val weight = List(8000, 500, 600)
    //val profit = List(10, 6, 5)

    //val capa = 50
    //val weight = List(15, 9, 17, 25, 6, 7, 22, 8)
    //val profit = List(28, 8, 37, 18, 7, 10, 32, 31)

    //val problem = KnapSack(capa, profit, weight)

    val problem = KnapSack("data/KnapSack/simple.txt")

    val solver: Solver =
      Solvers.layered.ddo(
        problem = problem,
        relaxation = KnapSackRelaxation(),
        widthHeuristic = FixedWidth(2),
        frontier = Frontier,
        //verbosityLvl = Large,
        useCache = true,
        lowerBound = KnapSackFlb(problem),
        ranking = KnapSackRanking()

      )

    val solution: Solution =
      solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
        println("------ NEW BEST ------")
        println(stats)
        println(sol.mkString("[", ", ", "]"))
      })

    println(solution)
  }

}
