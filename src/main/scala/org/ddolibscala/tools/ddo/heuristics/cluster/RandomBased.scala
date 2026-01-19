package org.ddolibscala.tools.ddo.heuristics.cluster

object RandomBased {

  def apply[T](seed: Long): org.ddolib.ddo.core.heuristics.cluster.RandomBased[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.RandomBased[T](seed)
  }

}
