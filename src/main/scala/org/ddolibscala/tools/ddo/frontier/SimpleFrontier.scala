package org.ddolibscala.tools.ddo.frontier

import org.ddolib.modeling.StateRanking

object SimpleFrontier {

  def lastExactLayer[T](ranking: StateRanking[T]): org.ddolib.ddo.core.frontier.SimpleFrontier[T] =
    new org.ddolib.ddo.core.frontier.SimpleFrontier[T](
      ranking,
      org.ddolib.ddo.core.frontier.CutSetType.LastExactLayer
    )

  def apply[T](ranking: StateRanking[T]): org.ddolib.ddo.core.frontier.SimpleFrontier[T] =
    new org.ddolib.ddo.core.frontier.SimpleFrontier[T](
      ranking,
      org.ddolib.ddo.core.frontier.CutSetType.Frontier
    )

}
