package org.ddolibscala.example.misp

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Relaxation

class MispRelaxation(problem: MispProblem) extends Relaxation[Set[Int]] {

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
