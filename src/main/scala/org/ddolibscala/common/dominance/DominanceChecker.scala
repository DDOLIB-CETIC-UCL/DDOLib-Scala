package org.ddolibscala.common.dominance

import org.ddolibscala.modeling.Dominance

abstract class DominanceChecker[T](dominance: Dominance[T])
    extends org.ddolib.common.dominance.DominanceChecker[T](dominance) {

  override def updateDominance(state: T, depth: Int, objectiveValue: Double): Boolean
}
