package org.ddolibscala.example.misp

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Relaxation

/** Companion object of the [[MispRelaxation]] class.
  */
object MispRelaxation {

  /** Returns a relaxation strategy for the Maximum Independent Set Problem (MISP) to be used in
    * decision diagram optimization (DDO) algorithms.
    *
    * <p> This relaxation defines how to merge multiple states and how to adjust transition costs
    * when exploring the relaxed search space. </p>
    *
    * In this implementation:
    *   - The merged state is computed as the union of all given states, meaning all nodes that are
    *     available in at least one state are considered available in the merged state.
    *   - The edge relaxation does not modify the transition cost; it returns the original cost.
    *
    * @return
    *   a relaxation strategy for the MISP
    */
  def apply(): MispRelaxation = new MispRelaxation()
}

/** Implements a relaxation strategy for the Maximum Independent Set Problem (MISP) to be used in
  * decision diagram optimization (DDO) algorithms.
  *
  * <p> This relaxation defines how to merge multiple states and how to adjust transition costs when
  * exploring the relaxed search space. </p>
  *
  * In this implementation:
  *   - The merged state is computed as the union of all given states, meaning all nodes that are
  *     available in at least one state are considered available in the merged state.
  *   - The edge relaxation does not modify the transition cost; it returns the original cost.
  */
class MispRelaxation extends Relaxation[Set[Int]] {

  override def merge(statesToMerge: Iterable[Set[Int]]): Set[Int] = {
    var merged: Set[Int] = Set.empty
    for (state <- statesToMerge) merged = merged union state
    merged
  }

  override def relaxEdge(
    from: Set[Int],
    to: Set[Int],
    merged: Set[Int],
    decision: Decision,
    cost: Double
  ): Double = cost
}
