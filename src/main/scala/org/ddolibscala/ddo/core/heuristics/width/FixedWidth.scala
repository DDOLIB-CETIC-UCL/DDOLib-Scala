package org.ddolibscala.ddo.core.heuristics.width

object FixedWidth {
  def apply[T](width: Int): FixedWidth[T] = new FixedWidth(width)
}

class FixedWidth[T](width: Int) extends WidthHeuristic[T] {
  override def maximumWidth(sate: T): Int = width
}
