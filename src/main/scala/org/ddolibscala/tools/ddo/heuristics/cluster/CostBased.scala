package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolibscala.modeling.StateRanking

/** This strategy select the nodes based on the objective value of the best path leading to them. It
  * requires a problem-specific StateRanking comparator to break the ties between nodes of same
  * cost.
  *
  * @see
  *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/heuristics/cluster/CostBased.html]]
  *   for details
  */
object CostBased {

  /** Constructs a cost-based reduction strategy with a given state ranking
    *
    * @param ranking
    *   the problem specific state-ranking comparator used to break the ties between nodes of same
    *   cost.
    * @tparam T
    *   the type of states in the decision diagram
    * @return
    *   a cost-based reduction strategy
    */
  def apply[T](ranking: StateRanking[T]): org.ddolib.ddo.core.heuristics.cluster.CostBased[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.CostBased[T](ranking)
  }

}
