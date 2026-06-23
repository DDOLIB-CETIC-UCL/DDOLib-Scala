package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.modeling.{ExactModel, FastLowerBound, Problem}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/ExactSolver.html exact solver]]
  *
  * @note
  *   This solver generate a complete decision diagram. It must only be used on small instances for
  *   debug purpose.
  */
private[solver] object ExactSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/ExactSolver.html exact solver]]
    *
    * @param _problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param _lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param _dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param _verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param _debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @param _exportDot
    *   whether the generated diagram must be exported to DOT file
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver that generate a complete decision diagram to solve the problem
    */
  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
    _verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
    _debugMode: DebugMode = DebugMode.Off,
    _exportDot: Boolean = false
  ): Solver = {

    val model: ExactModel[T] = new ExactModel[T] {
      override def problem(): Problem[T] = _problem

      override def exportDot(): Boolean = _exportDot

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.ddo.core.solver.ExactSolver[T](model))
  }
}
