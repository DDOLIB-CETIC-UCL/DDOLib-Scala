package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolibscala.modeling.StateRanking

object Hybrid {

  def apply[T](
    ranking: StateRanking[T],
    distance: StateDistance[T],
    alpha: Double,
    seed: Long
  ): org.ddolib.ddo.core.heuristics.cluster.Hybrid[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.Hybrid[T](ranking, distance, alpha, seed)
  }

}
