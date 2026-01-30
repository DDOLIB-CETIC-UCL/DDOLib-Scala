package org.ddolibscala.tools.ddo.heuristics.width

/** Companion object of the [[FixedWidth]] class. */
object FixedWidth {

  /** Returns a heuristic that always returns a fixed maximum width for any state.
    *
    * @param width
    *   the fixed maximum width
    * @tparam T
    *   the type of state used to compute the layer width
    * @return
    *   a heuristic that always returns a fixed maximum width for any state
    */
  def apply[T](width: Int): FixedWidth[T] = new FixedWidth(width)
}

/** Implements a static maximum width heuristic for decision diagram or search-based algorithms. <p>
  * This heuristic always returns a fixed maximum width for any state, regardless of the depth or
  * characteristics of the state. It can be used to restrict the size of a decision diagram (DD) or
  * a layer in a search algorithm. </p>
  *
  * @param width
  *   the fixed maximum width
  * @tparam T
  *   the type of state used to compute the layer width
  */
class FixedWidth[T](width: Int) extends WidthHeuristic[T] {
  override def maximumWidth(state: T): Int = width
}
