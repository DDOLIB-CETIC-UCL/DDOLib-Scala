package be.cetic.ddolibscala.nolayer.modeling

/** Defines a dominance checking mechanism to prune search states in NoLayer models.
 *
 * @tparam T
 * the type of states
 */
trait NoLayerDominanceChecker[T] extends org.ddolib.nolayer.modeling.NoLayerDominanceChecker[T] {

  /** Evaluates if a state can be pruned based on previously seen states.
   *
   * @param state
   * the current state to check
   * @param value
   * the objective value associated with reaching the state
   * @return
   * `true` if the state is dominated and should be pruned; `false` otherwise
   */
  override def updateDominance(state: T, value: Double): Boolean

  /** Clears the internally cached states used for dominance checking.
   * This is useful to reset the checker's state between solver iterations.
   */
  override def clear(): Unit = ()
}