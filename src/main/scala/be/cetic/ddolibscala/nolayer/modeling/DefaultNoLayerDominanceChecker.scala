package be.cetic.ddolibscala.nolayer.modeling

/** Default implementation of the [[NoLayerDominanceChecker]] trait that always returns `false`.
 *
 * @tparam T
 * the type of states
 */
class DefaultNoLayerDominanceChecker[T] extends NoLayerDominanceChecker[T] {

  override def updateDominance(state: T, value: Double): Boolean = false
}