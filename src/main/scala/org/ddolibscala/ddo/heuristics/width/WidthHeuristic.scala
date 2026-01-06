package org.ddolibscala.ddo.heuristics.width

trait WidthHeuristic[T] extends org.ddolib.ddo.core.heuristics.width.WidthHeuristic[T] {

  override def maximumWidth(sate: T): Int
}
