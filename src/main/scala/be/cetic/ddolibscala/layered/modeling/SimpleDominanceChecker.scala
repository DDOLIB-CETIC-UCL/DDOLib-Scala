package be.cetic.ddolibscala.layered.modeling

import org.ddolib.layered.modeling.Dominance

/** 
 * 
 */
object SimpleDominanceChecker {
  def apply[T](
    dominance: Dominance[T],
    nbVars: Int
  ): org.ddolib.layered.modeling.SimpleDominanceChecker[T] =
    new org.ddolib.layered.modeling.SimpleDominanceChecker[T](dominance, nbVars)
}
