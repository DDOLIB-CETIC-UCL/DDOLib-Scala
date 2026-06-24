package be.cetic.ddolibscala.modeling.nolayer

/** Default implementation of the [[FastLowerBound]] trait that always returns `0.0`.
  *
  * <p> This implementation can be used as a placeholder or a fallback when no meaningful fast lower
  * bound heuristic is available for a given problem. </p>
  *
  * @tparam T
  *   the type of the state
  */
class DefaultFastLowerBound[T] extends FastLowerBound[T] {

  override def lowerBound(state: T): Double = 0.0
}
