package org.ddolibscala.tools.ddo.heuristics.variables

/** Companion object of the [[DefaultVariableHeuristic]] class */
object DefaultVariableHeuristic {

  /** Returns a default implementation of [[VariableHeuristic]] that selects the next variable to
    * branch on without applying any specific strategy.
    *
    * @tparam T
    *   the type representing the problem state
    * @return
    *   a default implementation of [[VariableHeuristic]] that selects the next variable to branch
    *   on without applying any specific strategy.
    */
  def apply[T](): DefaultVariableHeuristic[T] = new DefaultVariableHeuristic()

}

/** A default implementation of [[VariableHeuristic]] that selects the next variable to branch on
  * without applying any specific strategy. <p> This heuristic simply returns the first element
  * obtained from the provided `variables` iterable. Therefore, it does not guarantee any particular
  * or deterministic order, as the iteration order of an iterable. </p>
  *
  * <p>This class serves as a minimal or placeholder heuristic to ensure that the compilation or
  * search process can proceed when no specific variable ordering rule has been defined by the
  * user.</p>
  *
  * @tparam T
  *   the type representing the problem state
  */
class DefaultVariableHeuristic[T] extends VariableHeuristic[T] {

  override def next(variables: Iterable[Int], states: Iterable[T]): Int = variables.head
}
