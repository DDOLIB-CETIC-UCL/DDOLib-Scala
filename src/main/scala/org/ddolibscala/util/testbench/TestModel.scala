package org.ddolibscala.util.testbench

import org.ddolibscala.modeling.*
import org.ddolibscala.tools.dominance.{DefaultDominanceChecker, DominanceChecker}

class TestModel[T, P <: Problem[T]](val problem: P) {

  def flb: FastLowerBound[T] = DefaultFastLowerBound()

  def relaxation: Option[Relaxation[T]] = None

  def ranking: StateRanking[T] = DefaultStateRanking()

  def dominance: DominanceChecker[T] = DefaultDominanceChecker[T]()
  

}
