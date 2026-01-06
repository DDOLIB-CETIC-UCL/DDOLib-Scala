package org.ddolibscala.modeling

trait StateRanking[T] extends org.ddolib.modeling.StateRanking[T] {

  def rank(state1: T, state2: T): Int

  final override def compare(o1: T, o2: T): Int = rank(o1, o2)
}
