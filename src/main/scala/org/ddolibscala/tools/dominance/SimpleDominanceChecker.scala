package org.ddolibscala.tools.dominance

import org.ddolib.modeling.Dominance

object SimpleDominanceChecker {
  def apply[T](
    dominance: Dominance[T],
    nbVars: Int
  ): org.ddolib.common.dominance.SimpleDominanceChecker[T] =
    new org.ddolib.common.dominance.SimpleDominanceChecker[T](dominance, nbVars)
}
