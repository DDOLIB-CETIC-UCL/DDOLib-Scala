package org.ddolibscala.ddo.core.frontier

import org.ddolib.ddo.core.frontier.{CutSetType, SimpleFrontier}
import org.ddolib.modeling.StateRanking

object SimpleFrontier {

  def lastExactLayer[T](ranking: StateRanking[T]): SimpleFrontier[T] =
    new SimpleFrontier[T](ranking, CutSetType.LastExactLayer)

  def apply[T](ranking: StateRanking[T]): SimpleFrontier[T] =
    new SimpleFrontier[T](ranking, CutSetType.Frontier)

}
