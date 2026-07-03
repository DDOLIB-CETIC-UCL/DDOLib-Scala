package be.cetic.ddolibscala.nolayer.modeling

/** Heuristic defining a fast lower bound for states, without requiring layer or variable concepts.
  *
  * @tparam T
  *   the type of the state
  */
trait FastLowerBound[T] extends org.ddolib.nolayer.modeling.FastLowerBound[T] {

  /** Returns a very rough estimation (lower bound) of the remaining cost from the given state to
    * reach any valid target state.
    *
    * @param state
    *   the state for which the estimate is to be computed
    * @return
    *   a lower bound on the remaining cost
    */
  def lowerBound(state: T): Double

  // METHOD THAT CONVERTS SCALA OBJECTS TO JAVA OBJECTS

  /** Used by the solver. Converts the call from the Java engine to the Scala implementation.
    */
  final override def fastLowerBound(state: T): Double = lowerBound(state)
}
