package org.ddolibscala.modeling

/** Companion object of the [[StateRanking]] class. */
object DefaultStateRanking {

  /** Returns a default implementation of the [[StateRanking]] trait where all the states are
    * equals.
    *
    * @tparam T
    *   the type of states
    * @return
    *   a default implementation of the [[StateRanking]] trait where all the states are equals
    */
  def apply[T](): DefaultStateRanking[T] = new DefaultStateRanking()
}

/** Default implementation of the [[StateRanking]] trait where all the states are equals.
  *
  * <p>This implementation can be used as a placeholder or a fallback when no meaningful ranking is
  * available for a given problem.</p>
  *
  * @tparam T
  *   the type of states
  */
class DefaultStateRanking[T] extends StateRanking[T] {

  override def rank(state1: T, state2: T): Int = 0
}
