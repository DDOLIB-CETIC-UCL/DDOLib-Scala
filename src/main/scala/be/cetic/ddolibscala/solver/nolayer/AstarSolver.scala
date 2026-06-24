package be.cetic.ddolibscala.solver.nolayer

import be.cetic.ddolibscala.modeling.nolayer.*
import be.cetic.ddolibscala.solver.Solver
import be.cetic.ddolibscala.util.{DebugMode, VerbosityLvl}
import org.ddolib.modeling.nolayer.Model
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel

/** Defines factory for a NoLayer
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/astar/core/solver/nolayer/AStarSolver.html A* solver]]
  */
private[solver] object AstarSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/astar/core/solver/nolayer/AStarSolver.html A* solver]]
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
      override def problem(): org.ddolib.modeling.nolayer.Problem[T] = _problem

      override def lowerBound(): org.ddolib.modeling.nolayer.FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): org.ddolib.common.dominance.NoLayerDominanceChecker[T] = _dominance

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.solving.astar.core.solver.nolayer.AStarSolver[T](model))
  }
}
