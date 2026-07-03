package be.cetic.ddolibscala.examples.nolayer.misp

import be.cetic.ddolibscala.nolayer.modeling.FastLowerBound

import scala.collection.immutable.BitSet

class MispFlb(problem: MispProblem) extends FastLowerBound[BitSet] {

  override def lowerBound(state: BitSet): Double =
    -state.foldLeft(0)((acc, node) => acc + problem.weights(node))
}
