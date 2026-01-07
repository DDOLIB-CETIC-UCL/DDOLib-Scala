package org.ddolibscala.example.misp

import org.ddolibscala.modeling.Dominance

/** Companion object of the [[MispDominance]] class. */
object MispDominance {

  /** Returns a dominance rule for the MISP.
    *
    * @return
    *   a dominance rule for the MISP
    */
  def apply(): MispDominance = new MispDominance()
}

/** Implementation of a dominance relation for the Maximum Independent Set Problem (MISP).
  *
  * <p> In this context, one state `state1` is considered dominated by another state `state2` if all
  * vertices selected in `state1` are also selected in `state2`. This allows pruning suboptimal
  * states during the search. </p>
  */
class MispDominance extends Dominance[Set[Int]] {

  override def key(state: Set[Int]) = 0

  override def isDominatedOrEqual(state1: Set[Int], state2: Set[Int]): Boolean =
    state1.subsetOf(state2)
}
