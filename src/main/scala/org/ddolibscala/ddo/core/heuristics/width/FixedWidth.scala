package org.ddolibscala.ddo.core.heuristics.width

import org.ddolib.ddo.core.heuristics.width.FixedWidth

object FixedWidth {

  def apply[T](width: Int): FixedWidth[T] = new FixedWidth(width)

}
