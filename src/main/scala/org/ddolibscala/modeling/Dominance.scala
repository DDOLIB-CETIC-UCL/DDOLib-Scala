package org.ddolibscala.modeling

/** Defines a dominance relation used to compare and prune states during the exploration of decision
  * diagrams or search spaces. <br>
  *
  * `Dominance` relation provides a way to determine whether one state of the problem is at least as
  * good as (i.e., dominates) another, allowing the solver to discard redundant or inferior
  * subproblems.<br>
  *
  * The dominance check is an essential optimization mechanism in decision diagram–based solvers, as
  * it helps reduce the search space by recognizing equivalent or dominated states that do not need
  * to be further expanded.
  *
  * @tparam T
  *   the type representing the problem state
  */
trait Dominance[T] extends org.ddolib.modeling.Dominance[T] {

  /** Returns a canonical key associated with a given state.<br>
    *
    * <p> This key is typically used to identify equivalent states or to group states that share the
    * same dominance characteristics. Implementations should ensure that two states with identical
    * keys are comparable under the same dominance criteria. </p>
    *
    * @param state
    *   the state for which a dominance key is requested
    * @return
    *   an object uniquely (or canonically) representing the given state
    */
  def key(state: T): Any

  /** Test whether `state1` is dominated by or equivalent to `state2`.<br>
    *
    * <p> A state `state1` is said to be dominated by `state2` if every feasible continuation from
    * `state1` cannot yield a better objective value than one from `state2`. In other words,
    * `state2` is at least as good as `state1` in all relevant aspects of the problem. </p>
    *
    * @param state1
    *   the first state to compare
    * @param state2
    *   the second state to compare against
    * @return
    *   - `true` if `state1` is dominated by or equivalent to `state2`
    *   - `false` otherwise
    */
  override def isDominatedOrEqual(state1: T, state2: T): Boolean

  // METHOD TO CONVERT SCALA OBJECTS TO JAVA OBJECT

  /** Used by the solver. Convert the input and output of [[key]] from Java to Scala and vice versa.
    */
  final override def getKey(state: T): AnyRef = {
    key(state).asInstanceOf[AnyRef]
  }
}
