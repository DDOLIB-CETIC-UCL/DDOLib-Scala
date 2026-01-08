package org.ddolibscala.modeling

object DefaultStateRanking {
  def apply[T](): DefaultStateRanking[T] = new DefaultStateRanking()
}

class DefaultStateRanking[T] extends StateRanking[T] {

  override def rank(state1: T, state2: T): Int = 0
}
