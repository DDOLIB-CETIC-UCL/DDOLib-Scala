package be.cetic.ddolibscala.examples.layered.knapsack

import be.cetic.ddolibscala.layered.modeling.StateRanking

/** Implements a ranking strategy for states in the KnapSack Problem.
 *
 * <p> The ranking is based on the fact that low capacity offers lower payoff  </p>
 */
class KnapSackRanking extends StateRanking[Int] {
  override def rank(state1: Int, state2: Int): Int = state2.compare(state1)
}// # Low capacity offers lower payoff