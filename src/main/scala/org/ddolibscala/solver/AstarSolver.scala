package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.modeling.{FastLowerBound, Model, Problem}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.DefaultFastLowerBound
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/astar/core/solver/AStarSolver.html A* solver]]
  */
trait AstarSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/astar/core/solver/AStarSolver.html A* solver]]
    *
    * @param problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
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
    *   a solver based on the A* algorithm
    */
  def apply[T](
    problem: Problem[T],
    lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
    variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    verbosityLvl: VerbosityLvl = VerbosityLvl.SILENT,
    debugMode: DebugMode = DebugMode.OFF
  ): Solver = {

    initSolver(problem, lowerBound, dominance, variableHeuristic, verbosityLvl, debugMode)
  }

  /** Internal method that initialize the solver  allowing simpler parameters' name in the `apply` method. */
  private def initSolver[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T],
    _dominance: DominanceChecker[T],
    _variableHeuristic: VariableHeuristic[T] ,
    _verbosityLvl: VerbosityLvl ,
    _debugMode: DebugMode
  ): Solver = {

    val model: Model[T] = new Model[T] {
      override def problem(): Problem[T] = _problem

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.astar.core.solver.AStarSolver[T](model))
  }

}
