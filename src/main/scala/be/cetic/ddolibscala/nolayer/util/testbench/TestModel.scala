package be.cetic.ddolibscala.nolayer.util.testbench

import be.cetic.ddolibscala.nolayer.modeling.*

/** Defines the model configuration and components used to solve a problem instance during NoLayer tests.
 *
 * This trait acts as a container for the specific strategies (Lower Bound, Dominance) 
 * required to solve a problem without using layers.
 *
 * @tparam S
 * the type of the state in the problem
 */
trait TestModel[S] {

  /** Specifies the Fast Lower Bound (FLB) implementation to use.
   *
   * @return
   * the fast lower bound instance, defaulting to [[DefaultFastLowerBound]].
   */
  def flb: FastLowerBound[S] = new DefaultFastLowerBound[S]()

  /** Specifies the dominance checking strategy to prune dominated states.
   *
   * @return
   * the dominance checker instance, defaulting to [[DefaultNoLayerDominanceChecker]].
   */
  def dominance: NoLayerDominanceChecker[S] = new DefaultNoLayerDominanceChecker[S]()

}