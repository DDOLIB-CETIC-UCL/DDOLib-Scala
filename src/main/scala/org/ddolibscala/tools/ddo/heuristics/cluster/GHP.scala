package org.ddolibscala.tools.ddo.heuristics.cluster

import scala.util.Random

/** Generalized Hyperplane Partitioning (GHP) reduction strategy for decision diagram layers.
  *
  * <p> This class implements [[ReductionStrategy]] and clusters nodes in a layer using a
  * distance-based partitioning method inspired by hyperplane separation. It requires a
  * problem-specific [[StateDistance]] function to compute distances between states.</p>
  *
  * <p>The GHP strategy works by: </p>
  *   1. Selecting two distant pivot nodes from the layer ;
  *   1. Assigning each remaining node to the cluster of the closer pivot ;
  *   1. Recursively splitting clusters until the desired number of clusters (`maxWidth`) is reached
  *
  * <p>A random number generator is used for tie-breaking and initial shuffling of the layer.</p>
  *
  * @see
  *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/heuristics/cluster/GHP.html]]
  *   for details
  */
object GHP {

  /** Constructs a GHP reduction strategy with a specified random seed.
    *
    * @param distance
    *   the distance function used to compare states
    * @param seed
    *   the seed used by the random number generator (random by default)
    * @tparam T
    *   the type of states in the decision diagram
    * @return
    *   a GHP reduction strategy
    */
  def apply[T](
    distance: StateDistance[T],
    seed: Long = Random.nextLong()
  ): org.ddolib.ddo.core.heuristics.cluster.GHP[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.GHP(distance, seed)
  }

}
