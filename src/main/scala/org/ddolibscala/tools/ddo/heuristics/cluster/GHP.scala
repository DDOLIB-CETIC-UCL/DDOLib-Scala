package org.ddolibscala.tools.ddo.heuristics.cluster

object GHP {

  def apply[T](distance: StateDistance[T]): org.ddolib.ddo.core.heuristics.cluster.GHP[T] = {
    new org.ddolib.ddo.core.heuristics.cluster.GHP(distance)
  }

}
