package org.ddolibscala.example.tsptw

import scala.collection.immutable.BitSet

case class TspTwState(
  position: Position,
  time: Int,
  mustVisit: BitSet,
  maybeVisit: BitSet,
  depth: Int
) {
  override def toString: String = {
    s"position: $position - time: $time - must visit: ${mustVisit.mkString("{", ", ", "}")}" +
      s" - maybe visit: ${maybeVisit.mkString("{", ",", "}")} - depth: $depth"
  }
}
