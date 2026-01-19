package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolibscala.modeling.StateRanking

object CostBased {

  def apply[T](ranking: StateRanking[T]): org.ddolib.ddo.core.heuristics.cluster.CostBased[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.CostBased[T](ranking)
  }

}
