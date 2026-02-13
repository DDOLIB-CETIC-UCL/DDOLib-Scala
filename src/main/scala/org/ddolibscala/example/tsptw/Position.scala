package org.ddolibscala.example.tsptw

/** Trait to model the position of a vehicle in a [[TspTwProblem]] */
sealed trait Position {}

/** Unique position of the vehicle.
  *
  * @param value
  *   a position of the vehicle in the current route
  */
case class TspNode(value: Int) extends Position {
  override def toString: String = value.toString
}

/** Used for merged states. The vehicle can be at all the position of the merged states.
  *
  * @param nodes
  *   all the position of the merged states.
  */
case class VirtualNodes(nodes: Set[Int]) extends Position {
  override def toString: String = nodes.toString()
}
