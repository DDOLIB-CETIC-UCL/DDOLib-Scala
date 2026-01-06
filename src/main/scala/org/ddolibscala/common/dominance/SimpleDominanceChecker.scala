package org.ddolibscala.common.dominance

import org.ddolib.common.dominance.SimpleDominanceChecker
import org.ddolib.modeling.Dominance

object SimpleDominanceChecker {
  def apply[T](dominance: Dominance[T], nbVars: Int): SimpleDominanceChecker[T] =
    new SimpleDominanceChecker[T](dominance, nbVars)
}
