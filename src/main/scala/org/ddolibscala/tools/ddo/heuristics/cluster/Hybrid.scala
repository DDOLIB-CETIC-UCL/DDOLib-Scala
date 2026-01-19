package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolibscala.modeling.StateRanking

/** Hybrid reduction strategy that combines cost-based and distance-based clustering for decision
  * diagram layers. This strategy is a hybridation between cost based selection and GHP. It
  * preserves the `w * alpha` best nodes (alpha between 0 and 1) and merge the other nodes using
  * clustering. It requires a problem-specific StateRanking comparator to break the ties between
  * nodes of same cost, and a problem-specif StateDistance to quantify the dissimilarity between
  * states.
  *
  * <p> This class implements [[ReductionStrategy]] and merges two strategies: </p>
  *   - [[CostBased]]: preserves a fraction of nodes based on their ranking
  *   - [[GHP]]: clusters the remaining nodes based on a distance metric
  *
  * <p>The combination is controlled by a weighting parameter `alpha`: </p>
  *   - `alpha` fraction of the clusters are preserved using cost-based ranking ;
  *   - `1 - alpha` fraction of the clusters are formed using the GHP distance-based method.
  *
  * @see
  *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/heuristics/cluster/Hybrid.html]]
  *   for details
  */
object Hybrid {

  /** Constructs a Hybrid reduction strategy with specified ranking, distance, alpha, and seed.
    *
    * @param ranking
    *   state ranking used for cost-based preservation
    * @param distance
    *   state distance used for GHP clustering
    * @param alpha
    *   fraction of clusters preserved using cost-based strategy
    * @param seed
    *   random seed for distance-based clustering
    * @tparam T
    *   the type of states in the decision diagram
    * @return
    *   a Hybrid reduction strategy
    */
  def apply[T](
    ranking: StateRanking[T],
    distance: StateDistance[T],
    alpha: Double,
    seed: Long
  ): org.ddolib.ddo.core.heuristics.cluster.Hybrid[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.Hybrid[T](ranking, distance, alpha, seed)
  }

}
