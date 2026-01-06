package org.ddolibscala.common.dominance

import org.ddolibscala.modeling.Dominance

object DefaultDominanceChecker {
  def apply[T](): DefaultDominanceChecker[T] = new DefaultDominanceChecker()
}

class DefaultDominanceChecker[T]
    extends DominanceChecker[T](new Dominance[T]() {
      override def key(state: T): Any = None

      override def isDominatedOrEqual(state1: T, state2: T): Boolean = false
    }) {

  override def updateDominance(state: T, depth: Int, objectiveValue: Double): Boolean = false
}
