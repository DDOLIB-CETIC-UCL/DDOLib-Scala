package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.StateRanking

object TspTwRanking {
  def apply(): TspTwRanking = new TspTwRanking()
}

/** Ranking class for states in the Traveling Salesperson Problem with Time Windows (TSPTW).
  *
  * <p> This class implements [[org.ddolibscala.modeling.StateRanking]] for `TSPTWState` and is used to order states within
  * the same layer of a decision diagram. The ranking helps identify which states are better
  * candidates for merging in a relaxed decision diagram. </p>
  *
  * <p> The comparison is based on the number of nodes in the `maybeVisit` set: states with more
  * nodes in `maybeVisit` are considered better candidates for merging and are ranked higher. </p>
  */
class TspTwRanking extends StateRanking[TspTwState] {

  override def rank(state1: TspTwState, state2: TspTwState): Int =
    -state1.maybeVisit.size.compare(state2.maybeVisit.size)
}
