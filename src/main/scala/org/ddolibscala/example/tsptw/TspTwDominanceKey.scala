package org.ddolibscala.example.tsptw

import scala.collection.immutable.BitSet

/** Key used for dominance checking in the Traveling Salesperson Problem with Time Windows (TSPTW).
  *
  * <p> A TspTwDominanceKey uniquely identifies a group of states that share the same current
  * position and the same set of locations that still must be visited . It is used by
  * [[TspTwDominance]]to determine which states can be compared for dominance. </p>
  *
  * <p> Two states with the same dominance key are comparable: the state with the lower current time
  * dominates the other, allowing pruning in the search. </p>
  *
  * @param position
  *   the current position in the tour
  * @param mustVisit
  *   the set of locations that must still be visited
  */
case class TspTwDominanceKey(position: Position, mustVisit: BitSet) {
  override def toString = s"position: $position - must visit: $mustVisit"
}
