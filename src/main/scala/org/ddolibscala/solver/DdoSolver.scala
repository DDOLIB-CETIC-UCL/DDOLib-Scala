package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.frontier.Frontier
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling._
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.ddo.frontier.SimpleFrontier
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker

trait DdoSolver {

  def apply[T](
    _problem: Problem[T],
    _relaxation: Relaxation[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
    _ranking: StateRanking[T] = (_: T, _: T) => 0,
    _widthHeuristic: WidthHeuristic[T] = FixedWidth[T](10),
    _frontier: FrontierType = FrontierType.LastExactLayer,
    _useCache: Boolean = false,
    _exportDot: Boolean = false,
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLevel = VerbosityLevel.SILENT,
    _debugMode: DebugMode = DebugMode.OFF
  ): Solver = {
    val model: DdoModel[T] = new DdoModel[T] {

      override def problem(): Problem[T] = _problem

      override def relaxation(): Relaxation[T] = _relaxation

      override def ranking(): StateRanking[T] = _ranking

      override def widthHeuristic(): WidthHeuristic[T] = _widthHeuristic

      override def frontier(): Frontier[T] = {
        _frontier match {
          case FrontierType.Frontier       => SimpleFrontier[T](ranking())
          case FrontierType.LastExactLayer => SimpleFrontier.lastExactLayer[T](ranking())
          case x => throw new IllegalArgumentException(s"Unhandled case: $x")
        }
      }

      override def useCache(): Boolean = _useCache

      override def exportDot(): Boolean = _exportDot

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl

      override def debugMode(): DebugLevel = _debugMode
    }

    new Solver(new org.ddolib.ddo.core.solver.SequentialSolver[T](model))
  }

}
