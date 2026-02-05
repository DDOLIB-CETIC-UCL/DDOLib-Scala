package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.FastLowerBound

import scala.collection.immutable.BitSet

/** Companion object of the [[TspTwFlb]] class. */
object TspTwFlb {

  /** Returns an object that computes a lower bound for the Traveling Salesperson Problem with Time
    * Windows (TSPTW).
    *
    * @param problem
    *   the associated TSPTW problem instance
    * @return
    *   an object that computes a lower bound for the TSPTW
    */
  def apply(problem: TspTwProblem): TspTwFlb = new TspTwFlb(problem)
}

/** Implementation of a fast lower bound for the Traveling Salesperson Problem with Time Windows
  * (TSPTW).
  *
  * <p> This class provides a heuristic lower bound on the total tour cost starting from a given
  * [[TspTwState]]. The lower bound is computed by summing the shortest available edges from the
  * current position, including all mandatory nodes that must be visited and a selection of optional
  * nodes if needed to complete the tour. The bound also considers returning to the depot and
  * respects time window constraints. </p>
  *
  * <p> If any mandatory node is unreachable from the current state, or if completing the tour is
  * impossible within the time windows, the bound returns [[scala.Int.MaxValue]] to indicate
  * infeasibility. </p>
  *
  * <p> Precomputes the cheapest outgoing edge for each node to speed up repeated lower bound
  * calculations. </p>
  *
  * @param problem
  *   the associated TSPTW problem instance
  */
class TspTwFlb(problem: TspTwProblem) extends FastLowerBound[TspTwState] {

  private val Infinity: Int             = Int.MaxValue
  private val numVar: Int               = problem.nbVars()
  private val cheapestEdges: Array[Int] =
    Array.tabulate(numVar)(i =>
      (0 until numVar).iterator.filter(_ != i).map(problem.timeMatrix(i)).min
    )

  override def lowerBound(state: TspTwState, variables: Iterable[Int]): Double = if (
    state.mustVisit.exists(!problem.reachable(state, _))
  ) Infinity.toDouble
  else {
    val mustTravelCost: Int = state.mustVisit.iterator.map(cheapestEdges).sum
    val completeTour: Int   = numVar - state.depth - 1 - state.mustVisit.size

    costsFromMaybe(state, completeTour) match {
      case None                  => Infinity.toDouble
      case Some(maybeTravelCost) =>
        computeFinalCost(state, mustTravelCost + maybeTravelCost, completeTour)
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

  private def computeFinalCost(state: TspTwState, travelCost: Int, numToCompleteTour: Int) = {

    val nodesToRoute: BitSet =
      if (numToCompleteTour <= 0) state.mustVisit else state.mustVisit union state.maybeVisit

    val backToDepot: Int = nodesToRoute.iterator
      .map(problem.timeMatrix(_)(0))
      .minOption
      .getOrElse(problem.minDuration(state, 0))

    val total: Int = travelCost + backToDepot
    if (state.time + total > problem.timeWindows(0).end) Infinity
    else total
  }

}
