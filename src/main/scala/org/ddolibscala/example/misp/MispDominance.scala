package org.ddolibscala.example.misp

import org.ddolibscala.modeling.Dominance

object MispDominance {
  def apply(): MispDominance = new MispDominance()
}

class MispDominance extends Dominance[Set[Int]] {

  override def key(state: Set[Int]): Any = 0

  override def isDominatedOrEqual(state1: Set[Int], state2: Set[Int]): Boolean =
    state1.subsetOf(state2)
}
