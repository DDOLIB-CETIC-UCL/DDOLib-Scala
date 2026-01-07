package org.ddolibscala.example.misp

import org.ddolibscala.modeling.StateRanking

/** Companion object of the [[MispRanking]] class. */
object MispRanking {

  /** Returns a ranking strategy for states in the Maximum Independent Set Problem (MISP).
    *
    * <p> The ranking is based on the number of remaining nodes in the state: a state with more
    * remaining nodes is considered more promising for exploration in a decision diagram. </p>
    *
    * @return
    *   a ranking strategy fir the MISP
    */
  def apply(): MispRanking = new MispRanking()
}

/** Implements a ranking strategy for states in the Maximum Independent Set Problem (MISP).
  *
  * <p> The ranking is based on the number of remaining nodes in the state: a state with more
  * remaining nodes is considered more promising for exploration in a decision diagram. </p>
  */
class MispRanking extends StateRanking[Set[Int]] {

  override def rank(state1: Set[Int], state2: Set[Int]): Int = {
    state1.size.compare(state2.size)
  }

}
