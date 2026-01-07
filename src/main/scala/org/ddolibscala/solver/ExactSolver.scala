package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.Decision
import org.ddolib.ddo.core.solver.ExactSolver
import org.ddolib.modeling._
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.dominance.DefaultDominanceChecker

import java.util

object ExactSolver {

  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound(),
    _dominance: DominanceChecker[T] = DefaultDominanceChecker(),
    _verbosityLevel: VerbosityLevel = VerbosityLevel.SILENT,
    _debugMode: DebugMode = DebugMode.OFF,
    _exportDot: Boolean = false
  ): Solver = {

    // Relaxation is not used by the exact by asked by the MDD. We can use relaxation that does nothing
    val dummyRelaxation = new Relaxation[T] {
      override def mergeStates(iterator: util.Iterator[T]): T = ???

      override def relaxEdge(t: T, t1: T, t2: T, decision: Decision, v: Double): Double = ???
    }

    val model: DdoModel[T] = new DdoModel[T] {
      override def problem(): Problem[T] = _problem

      override def relaxation(): Relaxation[T] = dummyRelaxation

      override def exportDot(): Boolean = _exportDot

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def verbosityLevel(): VerbosityLevel = _verbosityLevel

      override def debugMode(): DebugLevel = _debugMode
    }

    new ExactSolver[T](model)

  }

}
