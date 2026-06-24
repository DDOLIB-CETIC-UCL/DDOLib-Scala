package be.cetic.ddolibscala.example.misp

import be.cetic.ddolibscala.modeling
import be.cetic.ddolibscala.modeling.FastLowerBound

import scala.collection.immutable.BitSet

/** Compute a [[modeling.FastLowerBound]] for the Maximum Independent Set Problem
  * (MISP).
  *
  * @param problem
  *   the associated MISP problem instance
  */
class MispFlb(problem: MispProblem) extends FastLowerBound[BitSet] {

  override def lowerBound(state: BitSet, variables: Iterable[Int]): Double =
    -state.foldLeft(0)((acc, node) => acc + problem.weights(node))
}
