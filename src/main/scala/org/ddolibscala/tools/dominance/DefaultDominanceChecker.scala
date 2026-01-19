package org.ddolibscala.tools.dominance

import org.ddolibscala.modeling.Dominance

/** Companion object of the [[DefaultDominanceChecker]] class */
object DefaultDominanceChecker {

  /** Returns a default of a [[DominanceChecker]] that performs no dominance checking.
    *
    * @tparam T
    *   the type of states
    * @return
    *   a default of a [[DominanceChecker]] that performs no dominance checking
    */
  def apply[T](): DefaultDominanceChecker[T] = new DefaultDominanceChecker()
}

/** Default implementation of a [[DominanceChecker]] that performs no dominance checking. <p> This
  * class can be used as a placeholder when dominance pruning is not required or when a problem does
  * not define any dominance relation between states. </p>
  *
  * <p>In decision diagram or search-based algorithms, a dominance checker is used to compare two
  * states and discard those that are dominated (i.e., guaranteed to lead to no better solution).
  * This default implementation disables that feature: it never reports any dominance and never
  * removes any state.</p>
  *
  * @tparam T
  *   the type of states
  */
class DefaultDominanceChecker[T]
    extends DominanceChecker[T](new Dominance[T]() {
      override def key(state: T): Any = None

      override def isDominatedOrEqual(state1: T, state2: T): Boolean = false
    }) {

  override def updateDominance(state: T, depth: Int, objectiveValue: Double): Boolean = false
}
