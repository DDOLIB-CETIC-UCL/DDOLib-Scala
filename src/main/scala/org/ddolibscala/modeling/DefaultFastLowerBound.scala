package org.ddolibscala.modeling

/** Companion object of the [[DefaultFastLowerBound]] class. */
object DefaultFastLowerBound {

  /** Returns a default implementation of the [[FastLowerBound]] trait that always returns
    * `Int.MinValue`.
    *
    * @tparam T
    *   the type of the state
    * @return
    *   a default implementation of the [[FastLowerBound]] trait that always returns `Int.MinValue`
    */
  def apply[T]() = new DefaultFastLowerBound[T]()

}

/** Default implementation of the [[FastLowerBound]] trait that always returns `Int.MinValue`
  *
  * <p> This implementation can be used as a placeholder or a fallback when no meaningful fast lower
  * bound heuristic is available for a given problem. It effectively disables lower bound pruning
  * since the returned value is * the smallest possible integer. </p>
  *
  * @tparam T
  *   the type of the state
  */
class DefaultFastLowerBound[T] extends FastLowerBound[T] {

  override def lowerBound(state: T, variables: Iterable[Int]): Double = Int.MinValue.toDouble
}
