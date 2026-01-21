package org.ddolibscala.util.testbench

import org.ddolib.common.dominance.DominanceChecker
import org.ddolibscala.modeling.*
import org.ddolibscala.tools.dominance.DefaultDominanceChecker

/** Defines the model configuration and components used to solve a problem instance during tests.
  *
  * This trait acts as a container for the specific strategies (Lower Bound, Relaxation, Ranking,
  * Dominance) required to solve a problem. It corresponds to the specific configuration needed to
  * instantiate the solver for a given test case.
  *
  * @tparam T
  *   the type of the state in the problem
  */
trait TestModel[T] {

  /** Specifies the Fast Lower Bound (FLB) implementation to use.
    *
    * @return
    *   the fast lower bound instance, defaulting to
    *   [[org.ddolibscala.modeling.DefaultFastLowerBound]]
    */
  def flb: FastLowerBound[T] = DefaultFastLowerBound()

  /** Specifies the Relaxation logic to use, if any.
    *
    * @return
    *   the relaxation implementation if the problem requires it, or `None` if no relaxation is
    *   defined
    */
  def relaxation: Option[Relaxation[T]] = None

  /** Specifies the state ranking heuristic used to select nodes when the width limit is reached.
    *
    * @return
    *   the state ranking strategy, defaulting to [[org.ddolibscala.modeling.DefaultStateRanking]]
    */
  def ranking: StateRanking[T] = DefaultStateRanking()

  /** Specifies the dominance checking strategy to prune dominated states.
    *
    * @return
    *   the dominance checker instance, defaulting to
    *   [[org.ddolibscala.tools.dominance.DefaultDominanceChecker]]
    */
  def dominance: DominanceChecker[T] = DefaultDominanceChecker[T]()

}
