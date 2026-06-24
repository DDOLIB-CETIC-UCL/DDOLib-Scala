package be.cetic.ddolibscala.solver

import be.cetic.ddolibscala.modeling.DefaultFastLowerBound
import be.cetic.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import be.cetic.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.solving.ddo.core.heuristics.variable.layered.VariableHeuristic
import org.ddolib.modeling.layered.{AwAstarModel, FastLowerBound, Problem}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import be.cetic.ddolibscala.util.VerbosityLvl.Silent
import be.cetic.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/awastar/core/solver/AwAstarSolver.html Anytime Weighted A* solver]]
  */
private[solver] object AwAstarSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/awastar/core/solver/AwAstarSolver.html Anytime Weighted A* solver]]
    *
    * @param _problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param _weight
    *   the weight used for the evaluation function (f = g + w*h)
    * @param _lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param _upperBound
    *   a precomputed upper used to start pruning earlier
    * @param _dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param _variableHeuristic
    *   the heuristic used to determine the next variable to branch on during decision diagram
    *   compilation
    * @param _verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param _debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver based on the Any-time Weighted A* algorithm
    */
  def apply[T](
    _problem: Problem[T],
    _weight: Double = 5,
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double = Double.PositiveInfinity,
    _dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLvl = Silent,
    _debugMode: DebugMode = DebugMode.Off
  ): Solver = {
    val model: AwAstarModel[T] = new AwAstarModel[T] {
      override def problem(): Problem[T] = _problem

      override def weight(): Double = _weight

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(org.ddolib.solving.awastar.core.solver.layered.AwAstarSolver[T](model))
  }

}
