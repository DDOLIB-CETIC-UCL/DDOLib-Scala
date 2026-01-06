package org.ddolibscala.example.misp

import org.ddolibscala.modeling.FastLowerBound

object MispFlb {
  def apply(problem: MispProblem): MispFlb = new MispFlb(problem)
}

class MispFlb(problem: MispProblem) extends FastLowerBound[Set[Int]] {

  override def lowerBound(state: Set[Int], variables: Iterable[Int]): Double =
    -state.map(node => problem.weight(node)).sum
}
