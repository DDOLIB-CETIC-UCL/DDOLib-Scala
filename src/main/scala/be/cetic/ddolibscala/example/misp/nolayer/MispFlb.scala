package be.cetic.ddolibscala.example.misp.nolayer

import be.cetic.ddolibscala.modeling.nolayer.FastLowerBound

import scala.collection.immutable.BitSet

class MispFlb(problem: MispProblem) extends FastLowerBound[BitSet] {

  override def lowerBound(state: BitSet): Double =
    -state.foldLeft(0)((acc, node) => acc + problem.weights(node))
}
