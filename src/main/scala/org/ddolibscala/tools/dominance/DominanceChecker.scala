package org.ddolibscala.tools.dominance

import org.ddolibscala.modeling.Dominance

/** Object that, given a dominance, will check if a state is dominated.
  *
  * @param dominance
  *   the problem specific dominance rule
  * @tparam T
  *   the type of states
  */
abstract class DominanceChecker[T](dominance: Dominance[T])
    extends org.ddolib.common.dominance.DominanceChecker[T](dominance) {

  /**
   * Checks whether the input state is dominated and updates the front of non-dominated nodes.
   *
   * @param state    the state on which test dominance
   * @param depth    the depth of the state in the MDD
   * @param objectiveValue the length of the longest path from the root to the input state.
   * @return whether the input state is dominated
   */
  override def updateDominance(state: T, depth: Int, objectiveValue: Double): Boolean
}
