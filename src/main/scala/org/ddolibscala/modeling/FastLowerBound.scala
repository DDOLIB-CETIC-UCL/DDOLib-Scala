package org.ddolibscala.modeling

import scala.jdk.CollectionConverters.CollectionHasAsScala

/** Heuristic defining a fast lower bound for states
  *
  * @tparam T
  *   the type of the state
  */
trait FastLowerBound[T] extends org.ddolib.modeling.FastLowerBound[T] {

  /** Returns a very rough estimation (lower bound) of the optimal value that could be reached if
    * state were the initial state.
    *
    * @param state
    *   the state for which the estimate is to be computed
    * @param variables
    *   the set of unassigned variables
    * @return
    *   avery rough estimation (lower bound) of the optimal value that could be reached if state
    *   were the initial state
    */
  def lowerBound(state: T, variables: Iterable[Int]): Double

  // METHOD THAT CONVERT SCALA OBJECTS TO JAVA OBJECTS

  /** Used by the solver. Convert the input and output of [[lowerBound]] from Java to Scala and vice
    * versa.
    */
  final override def fastLowerBound(state: T, variables: java.util.Set[java.lang.Integer]): Double =
    lowerBound(state, variables.asScala.view.map(_.intValue))
}
