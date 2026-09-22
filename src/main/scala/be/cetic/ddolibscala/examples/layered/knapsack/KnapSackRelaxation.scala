package be.cetic.ddolibscala.examples.layered.knapsack

import be.cetic.ddolibscala.layered.modeling.Relaxation
import org.ddolib.layered.solving.ddo.core.Decision

/** Relaxation operators for the Knapsack Problem.
 *
 * <p> Merging takes the maximum remaining capacity among the merged states, so that no
 * feasible decision is ever lost: any item that fit in one of the original states still fits
 * in the merged one. </p>
 * <p> The cost of an edge redirected to a merged node does not need to be adjusted. </p>
 */
class KnapSackRelaxation extends Relaxation[Int]{

  override def merge(statesToMerge: Iterable[Int]): Int = statesToMerge.max

  override def relaxEdge(from: Int, to: Int, merged: Int, decision: Decision, cost: Double): Double = cost
}