package org.ddolibscala.example.tsptw

import scala.collection.immutable.BitSet

case class TspTwDominanceKey (position: Position, mustVisit: BitSet)
