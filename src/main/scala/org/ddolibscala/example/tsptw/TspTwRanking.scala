package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.StateRanking

object TspTwRanking {
  def apply(): TspTwRanking = new TspTwRanking()
}

class TspTwRanking extends StateRanking[TspTwState] {

  override def rank(state1: TspTwState, state2: TspTwState): Int = {
    -state1.maybeVisit.size.compare(state2.maybeVisit.size)
  }
}
