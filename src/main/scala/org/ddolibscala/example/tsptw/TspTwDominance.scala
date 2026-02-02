package org.ddolibscala.example.tsptw

import org.ddolibscala.modeling.Dominance

object TspTwDominance {
  def apply(): TspTwDominance = new TspTwDominance()
}

class TspTwDominance extends Dominance[TspTwState] {

  override def key(state: TspTwState): TspTwDominanceKey =
    TspTwDominanceKey(state.position, state.mustVisit)

  override def isDominatedOrEqual(state1: TspTwState, state2: TspTwState): Boolean =
    state1.time >= state2.time
}
