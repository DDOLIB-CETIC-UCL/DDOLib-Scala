package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.FastLowerBound

object TspTwFlb {
  def apply(problem: TspTwProblem): TspTwFlb = new TspTwFlb(problem)
}

class TspTwFlb(problem: TspTwProblem) extends FastLowerBound[TspTwState] {

  private val Infinity: Int             = Int.MaxValue
  private val numVar: Int               = problem.nbVars()
  private val cheapestEdges: Array[Int] =
    Array.tabulate(numVar)(i => (0 until numVar).iterator.filter(_ != i).map(problem.time(i)).min)

  override def lowerBound(state: TspTwState, variables: Iterable[Int]): Double = {
    if (state.mustVisit.exists(!problem.reachable(state, _))) Infinity.toDouble
    else {
      val mustTravelCost = state.mustVisit.iterator.map(cheapestEdges).sum
      val completeTour   = numVar - state.depth - 1 - state.mustVisit.size

      costsFromMaybe(state, completeTour) match {
        case None                  => Infinity.toDouble
        case Some(maybeTravelCost) =>
          computeFinalCost(state, mustTravelCost + maybeTravelCost)
      }
    }
  }

  private def costsFromMaybe(state: TspTwState, numToCompleteTour: Int): Option[Int] = {
    if (numToCompleteTour <= 0) Some(0)
    else {
      val candidates = state.maybeVisit.filter(problem.reachable(state, _))
      if (candidates.size < numToCompleteTour) None
      else Some(candidates.map(cheapestEdges).toSeq.sorted.take(numToCompleteTour).sum)
    }
  }

  private def computeFinalCost(state: TspTwState, travelCost: Int): Int = {
    val backToDepot = (state.mustVisit union state.maybeVisit).iterator
      .map(problem.time(_)(0))
      .minOption
      .getOrElse(Infinity)

    val (startCost, backCost) = {
      if (travelCost == 0) (0, problem.minDuration(state, 0))
      else {
        val s = state.position match {
          case TspNode(node)       => cheapestEdges(node)
          case VirtualNodes(nodes) => nodes.iterator.map(n => cheapestEdges(n)).min
        }
        (s, backToDepot)
      }
    }

    val total = startCost + travelCost + backCost
    if (state.time + total > problem.timeWindows(0).end) Infinity
    else total
  }

}
