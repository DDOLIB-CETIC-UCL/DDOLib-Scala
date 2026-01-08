package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.modeling.{AcsModel, FastLowerBound, Problem}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.VerbosityLvl.Silent
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/acs/core/solver/ACSSolver.html Anytime Column Search (ACS) solver]]
  */
object AcsSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/acs/core/solver/ACSSolver.html Anytime column search (ACS) solver]]
    *
    * @param problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param columnWidth
    *   column width used for formatted output during the Anytime Column Search process.
    * @param lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param variableHeuristic
    *   the heuristic used to determine the next variable to branch on during decision diagram
    *   compilation
    * @param verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver based on the ACS algorithm
    */
  def apply[T](
    problem: Problem[T],
    columnWidth: Int = 5,
    lowerBound: FastLowerBound[T] = DefaultFastLowerBound(),
    dominance: DominanceChecker[T] = DefaultDominanceChecker(),
    variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic(),
    verbosityLvl: VerbosityLvl = Silent,
    debugMode: DebugMode = DebugMode.Off
  ): Solver = {
    initSolver(
      problem,
      columnWidth,
      lowerBound,
      dominance,
      variableHeuristic,
      verbosityLvl,
      debugMode
    )

  }

  /** Internal method that initializes the solver allowing simpler parameters' name in the `apply`
    * method.
    */
  private def initSolver[T](
    _problem: Problem[T],
    _columnWidth: Int,
    _lowerBound: FastLowerBound[T],
    _dominance: DominanceChecker[T],
    _variableHeuristic: VariableHeuristic[T],
    _verbosityLvl: VerbosityLvl,
    _debugMode: DebugMode
  ): Solver = {

    val model: AcsModel[T] = new AcsModel[T] {
      override def problem(): Problem[T] = _problem

      override def columnWidth(): Int = _columnWidth

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.acs.core.solver.ACSSolver[T](model))

  }

}
