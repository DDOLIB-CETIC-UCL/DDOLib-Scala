package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.layered.modeling.DefaultFastLowerBound
import be.cetic.ddolibscala.layered.heuristics.variable.DefaultVariableHeuristic
import be.cetic.ddolibscala.layered.modeling.DefaultDominanceChecker
import be.cetic.ddolibscala.common.util.{DebugMode, VerbosityLvl}
import org.ddolib.layered.modeling.DominanceChecker
import org.ddolib.layered.modeling.{FastLowerBound, Model, Problem}
import org.ddolib.layered.solving.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.common.util.debug.DebugLevel
import org.ddolib.common.util.verbosity.VerbosityLevel

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/astar/core/solver/AStarSolver.html A* solver]]
  */
private[ddolibscala] object AstarSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/astar/core/solver/AStarSolver.html A* solver]]
    *
    * @param _problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
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
    *   a solver based on the A* algorithm
    */
  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double,
    _dominance: DominanceChecker[T] = new DefaultDominanceChecker[T](),
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
    _debugMode: DebugMode = DebugMode.Off
  ): Solver = {

    val model: Model[T] = new Model[T] {
      override def problem(): Problem[T] = _problem

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    Solver(new org.ddolib.layered.solving.astar.core.solver.AStarSolver[T](model))
  }
}
