package org.ddolibscala.example.misp

import org.ddolibscala.modeling.StateRanking

object MispRanking {
  def apply(): MispRanking = new MispRanking()
}

class MispRanking extends StateRanking[Set[Int]] {

  override def rank(state1: Set[Int], state2: Set[Int]): Int = {
    state1.size.compare(state2.size)
  }

}
