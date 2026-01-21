package org.ddolibscala.util.testbench

import org.ddolib.common.dominance.DominanceChecker
import org.ddolibscala.modeling.*
import org.ddolibscala.tools.dominance.DefaultDominanceChecker

trait SolverTestConfig[T]() {

  def flb: FastLowerBound[T] = DefaultFastLowerBound()

  def relaxation: Option[Relaxation[T]] = None

  def ranking: StateRanking[T] = DefaultStateRanking()

  def dominance: DominanceChecker[T] = DefaultDominanceChecker[T]()

}
