package org.ddolibscala.example.misp

import org.ddolibscala.modeling.FastLowerBound

import scala.collection.immutable.BitSet

/** Companion object of the [[MispFlb]] class.
  */
object MispFlb {

  /** Returns an object computing a lower bound for the Maximum Independent Set Problem (MISP).
    *
    * @param problem
    *   the associated MISP problem instance
    * @return
    *   an object computing a lower bound for the Maximum Independent Set Problem (MISP)
    */
  def apply(problem: MispProblem): MispFlb = new MispFlb(problem)
}

/** Compute a [[org.ddolibscala.modeling.FastLowerBound]] for the Maximum Independent Set Problem
  * (MISP).
  *
  * @param problem
  *   the associated MISP problem instance
  */
class MispFlb(problem: MispProblem) extends FastLowerBound[BitSet] {

  override def lowerBound(state: BitSet, variables: Iterable[Int]): Double =
    -state.map(node => problem.weights(node)).sum
}
