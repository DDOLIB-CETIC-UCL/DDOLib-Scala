package be.cetic.ddolibscala.tools.ddo.heuristics.cluster.layered

/** A simple random-based reduction strategy for decision diagram layers.
  *
  * <p> This class implements [[ReductionStrategy]]and generates clusters by randomly selecting
  * nodes from a layer. Each selected node forms its own cluster.</p>
  *
  * <p> The strategy is controlled by a `Random` object, which can be seeded to ensure reproducible
  * behavior.
  *
  * @see
  *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/heuristics/cluster/RandomBased.html]]
  *   for details
  */
object RandomBased {

  /** Constructs a random-based reduction strategy with a given seed.
    *
    * @param seed
    *   the seed for the random number generator
    * @tparam T
    *   the type of states in the decision diagram
    * @return
    *   a random-based reduction strategy
    */
  def apply[T](
    seed: Long
  ): org.ddolib.solving.ddo.core.heuristics.cluster.layered.RandomBased[T] = {
    new org.ddolib.solving.ddo.core.heuristics.cluster.layered.RandomBased[T](seed)
  }

}
