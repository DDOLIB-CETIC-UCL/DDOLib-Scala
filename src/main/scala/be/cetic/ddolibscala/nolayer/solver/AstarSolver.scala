package be.cetic.ddolibscala.nolayer.solver

import be.cetic.ddolibscala.nolayer.modeling.*
import be.cetic.ddolibscala.common.util.{DebugMode, VerbosityLvl}
import org.ddolib.nolayer.modeling.Model
import org.ddolib.common.util.debug.DebugLevel
import org.ddolib.common.util.verbosity.VerbosityLevel

/** Defines factory for a NoLayer
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solving/astar/core/solver/AStarSolver.html A* solver]]
  */
private[ddolibscala] object AstarSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solving/astar/core/solver/AStarSolver.html A* solver]]
    * operating without layer or fixed variable concepts.
    *
    * @param _problem
    *   the structure defining the transitions and objective function of the optimization task
    * @param _lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param _upperBound
    *   a precomputed upper bound used to start pruning earlier
    * @param _dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param _verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param _debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver based on the A* algorithm without layers
    */
  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double = Double.PositiveInfinity,
    _dominance: NoLayerDominanceChecker[T] = DefaultNoLayerDominanceChecker[T](),
    _verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
    _debugMode: DebugMode = DebugMode.Off
  ): Solver = {

    val model: Model[T] = new Model[T] {
      override def problem(): org.ddolib.nolayer.modeling.Problem[T] = _problem

      override def lowerBound(): org.ddolib.nolayer.modeling.FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): org.ddolib.nolayer.modeling.NoLayerDominanceChecker[T] = _dominance

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    Solver(new org.ddolib.nolayer.solving.astar.core.solver.AStarSolver[T](model))
  }
}
