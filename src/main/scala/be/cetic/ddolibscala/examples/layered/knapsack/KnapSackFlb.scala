package be.cetic.ddolibscala.examples.layered.knapsack

import be.cetic.ddolibscala.layered.modeling.FastLowerBound

/** Compute a [[be.cetic.ddolibscala.layered.modeling.FastLowerBound]] for the KnapSack Problem.
 *
 * @param problem
 *   the associated KnapSack problem instance
 */
class KnapSackFlb(problem: KnapSack) extends FastLowerBound[Int] {

  override def lowerBound(state: Int, variables: Iterable[Int]): Double = {
    // Sort remaining items by profit-to-weight ratio
    val sortedItems = variables.toList.sortBy(v => -problem.profit(v).toDouble / problem.weight(v))

    var remainingCapa = state
    var totalProfit = 0.0
    var i = 0

    while (i < sortedItems.length && remainingCapa > 0) {
      val v = sortedItems(i)
      val w = problem.weight(v)
      val p = problem.profit(v)

      if (w <= remainingCapa) {
        // full object
        totalProfit += p
        remainingCapa -= w
      } else {
        // fraction of the objet fill the bag
        totalProfit += p.toDouble * remainingCapa / w
        remainingCapa = 0
      }
      i += 1
    }

    -totalProfit
  }
}
