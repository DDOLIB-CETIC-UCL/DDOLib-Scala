package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.Dominance

/** Companion object of the [[TspTwDominance]] class.
  */
object TspTwDominance {

  /** Returns a dominance relation for the Traveling Salesperson Problem with Time Windows (TSPTW).
    *
    * @return
    *   a dominance relation for the Traveling Salesperson Problem with Time Windows (TSPTW).
    */
  def apply(): TspTwDominance = new TspTwDominance()
}

/** Dominance relation for the Traveling Salesperson Problem with Time Windows (TSPTW).
  *
  * <p> This class defines a dominance rule between two [[TspTwState]] instances. Two states are
  * comparable if they share the same current position and the same set of remaining locations to
  * visit `mustVisit`. Among such comparable states, the state with the lower current time dominates
  * the other. </p>
  *
  * <p> Dominance is used to prune the search space: if a state is dominated by another, it can be
  * safely discarded without losing optimality. </p>
  */
class TspTwDominance extends Dominance[TspTwState] {

  override def key(state: TspTwState): TspTwDominanceKey =
    TspTwDominanceKey(state.position, state.mustVisit)

  override def isDominatedOrEqual(state1: TspTwState, state2: TspTwState): Boolean =
    state1.time >= state2.time
}
