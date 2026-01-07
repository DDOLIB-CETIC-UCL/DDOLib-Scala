package org.ddolibscala
package solver

import org.ddolib.astar.core.solver.AStarSolver
import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.modeling.{FastLowerBound, Model, Problem}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.dominance.DefaultDominanceChecker

object AstarSolver {

  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound(),
    _dominance: DominanceChecker[T] = DefaultDominanceChecker(),
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic(),
    _verbosityLvl: VerbosityLevel = VerbosityLevel.SILENT,
    _debugMode: DebugMode = DebugMode.OFF
  ): Solver = {

    val model: Model[T] = new Model[T] {
      override def problem(): Problem[T] = _problem

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl

      override def debugMode(): DebugLevel = _debugMode
    }

    new AStarSolver[T](model)
  }

}
