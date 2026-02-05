package org.ddolibscala.example.tsptw

import scala.collection.immutable.BitSet

/** Represents a state in the dynamic programming model for the Traveling Salesperson Problem with
  * Time Windows (TSPTW). <p> Each state encapsulates the current information about the vehicle's
  * position, the set of nodes yet to visit, and timing information. This record is used both for
  * individual states and for relaxed/merged states. </p>
  *
  * @param position
  *   the current last position of the vehicle. Usually unique and represented by [[TspNode]]. In
  *   merged states, the vehicle can be "at any position at the same time," represented by
  *   [[VirtualNodes]]
  * @param time
  *   the arrival time of the vehicle at the current position
  * @param mustVisit
  *   a set representing all nodes that must still be visited
  * @param maybeVisit
  *   a set representing nodes that might have been visited or not in merged states
  * @param depth
  *   a depth of the layer containing this state in the dynamic programming model
  */
case class TspTwState(
  position: Position,
  time: Int,
  mustVisit: BitSet,
  maybeVisit: BitSet,
  depth: Int
) {
  override def toString: String =
    s"position: $position - time: $time - must visit: ${mustVisit.mkString("{", ", ", "}")}" +
      s" - maybe visit: ${maybeVisit.mkString("{", ",", "}")} - depth: $depth"
}
