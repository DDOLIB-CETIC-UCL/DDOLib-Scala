package org.ddolibscala.modeling

/** A state ranking is used to order the states and decides the ones that are kept and the ones that
  * are merged/deleted when a relaxation/restriction occurs. <br>
  *
  * In this context, a state ranking is nothing but an ordering on the states which is defined in
  * the form of a comparator. The solvers and MDD should interpret compare(a, b) > 0 as a should
  * have a higher chance of being kept intact while b should have a higher chance of being merged.
  *
  * @tparam T
  *   the type of states
  */
trait StateRanking[T] extends org.ddolib.modeling.StateRanking[T] {

  /** Defines how to compare two states.
    *
    * @param state1
    *   the first state to compare
    * @param state2
    *   the second state to compare
    * @return
    *   a value `x` where
    *   {{{
    *           x < 0 if state1 < state2
    *           x == 0 if state1 == state2
    *           x > 0 if state1 > state2
    *   }}}
    */
  def rank(state1: T, state2: T): Int

  /** Used by the solver. The java method called to compare two states. */
  final override def compare(o1: T, o2: T): Int = rank(o1, o2)
}
