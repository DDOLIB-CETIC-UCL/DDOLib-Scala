import be.cetic.ddolibscala.layered.modeling.Problem
import org.ddolib.layered.solving.ddo.core.Decision
import be.cetic.ddolibscala.layered.modeling.Relaxation
import be.cetic.ddolibscala.Solvers
import be.cetic.ddolibscala.SearchStatistic
import be.cetic.ddolibscala.common.frontier.CutSetType.Frontier
import be.cetic.ddolibscala.common.heuristics.width.FixedWidth
import be.cetic.ddolibscala.common.util.VerbosityLvl.Large
import be.cetic.ddolibscala.layered.solver.{Solution, Solver}
import be.cetic.ddolibscala.layered.modeling.FastLowerBound
import be.cetic.ddolibscala.layered.modeling.StateRanking



// MODELE DP
class KnapSack(val capa: Int, val profit: List[Int], val weight: List[Int]) extends Problem[Int] {

  override def nbVars(): Int = weight.length // une variable de decision par objet

  override def initialState(): Int = capa // la racine on a encore pris aucunes decision donc toute la capa est dispo

  override def initialValue(): Double = 0.0 // aucun profit encore accumulé on a pas prus d'objet

  override def domainValues(state: Int, variable: Int): Iterable[Int] = {
    if (state >= weight(variable)) List(0, 1)
    else List(0)
  }

  override def transition(state: Int, decision: Decision): Int = {
    state - weight(decision.variable()) * decision.value()
  }

  override def transitionCost(state: Int, decision: Decision): Double = {
    -profit(decision.variable()) * decision.value()
  }

  override def evaluate(solution: Array[Int]): Double = {
    -solution.indices.map(i => profit(i) * solution(i)).sum
  } // calcule le cout total

}

// RELAXATION
class KnapSackRelaxation extends Relaxation[Int]{

  override def merge(statesToMerge: Iterable[Int]): Int = statesToMerge.max

  override def relaxEdge(from: Int, to: Int, merged: Int, decision: Decision, cost: Double): Double = cost
}


// BORNE INFÉRIEURE
class KnapSackFlb(problem: KnapSack) extends FastLowerBound[Int] {
  override def lowerBound(state: Int, variables: Iterable[Int]): Double = {
    -variables.map(v => problem.profit(v)).sum
  }
}


// HEURISTIQUE DE TRI
class KnapSackRanking extends StateRanking[Int] {
  override def rank(state1: Int, state2: Int): Int = state1.compare(state2)
}// comme Quentin l'a dis : "petite capacité = moins intéressant"


//MAIN
def main(args: Array[String]): Unit = {
  //val capa = 8000
  //val weight = List(8000, 500, 600)
  //val profit = List(10, 6, 5)

  val capa = 50
  val weight = List(15, 9, 17, 25, 6, 7, 22, 8)
  val profit = List(28, 8, 37, 18, 7, 10, 32, 31)

  val problem = KnapSack(capa, profit, weight)

  val solver: Solver =
    Solvers.layered.ddo(
      problem = problem,
      relaxation = KnapSackRelaxation(),
      widthHeuristic = FixedWidth(2), // certains param sont erpris de l'exemple MISP sans adaptation
      frontier = Frontier,
      //verbosityLvl = Large,
      useCache = true,
      lowerBound = KnapSackFlb(problem) ,
      ranking = KnapSackRanking()

    )

  val solution: Solution =
    solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
      println("------ NEW BEST ------")
      println(stats)
      println(sol.mkString("[", ", ", "]"))
    })

}