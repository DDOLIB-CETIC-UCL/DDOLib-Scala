package org.ddolibscala.example.tsptw

sealed trait Position {}

case class TspNode(value: Int) extends Position {
  override def toString: String = value.toString
}

case class VirtualNodes(nodes: Set[Int]) extends Position {
  override def toString: String = nodes.toString()
}
