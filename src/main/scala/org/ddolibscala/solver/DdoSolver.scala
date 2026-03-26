package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.frontier.Frontier
import org.ddolib.ddo.core.heuristics.cluster.{ReductionStrategy, StateDistance}
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling.{DdoModel, FastLowerBound, Problem, Relaxation, StateRanking}
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.{DefaultFastLowerBound, DefaultStateRanking}
import org.ddolibscala.tools.ddo.frontier.{CutSetType, SimpleFrontier}
import org.ddolibscala.tools.ddo.heuristics.cluster.CostBased
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/SequentialSolver.html DDO solver]].
  */
private[solver] object DdoSolver {

  /** Instantiates and returns a
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/SequentialSolver.html DDO solver]].
    *
    * @param _problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param _relaxation
    *   the relaxation of the model used to evaluate the nodes or layers of the decision diagram.
    * @param _lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param _upperBound
    *   a precomputed upper used to start pruning earlier
    * @param _dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param _ranking
    *   the heuristic used to determine the next variable to branch on during decision diagram
    *   compilation
    * @param _widthHeuristic
    *   heuristic controlling the maximum number of nodes per layer
    * @param _frontier
    *   type of frontier management strategy used to store and expand the current layer of the
    *   decision diagram.
    * @param _useCache
    *   whether caching mechanism must be used
    * @param _exportDot
    *   whether the generated diagram must be exported to DOT file
    * @param _variableHeuristic
    *   the heuristic used to determine the next variable to branch on during decision diagram
    *   compilation
    * @param _verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param _debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @param _relaxStrategy
    *   strategy to select which nodes should be merged together on a relaxed DD
    * @param _restrictStrategy
    *   strategy to select which nodes should be dropped on a restricted DD
    * @param _stateDistance
    *   distance function between states, used to form clusters when deciding which nodes on a layer
    *   of a decision diagram should be merged.
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver based on the DDO algorithm
    */
  def apply[T](
    _problem: Problem[T],
    _relaxation: Relaxation[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double,
    _dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
    _ranking: StateRanking[T] = DefaultStateRanking[T](),
    _widthHeuristic: WidthHeuristic[T] = FixedWidth[T](10),
    _frontier: CutSetType = CutSetType.LastExactLayer,
    _useCache: Boolean = false,
    _exportDot: Boolean = false,
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
    _debugMode: DebugMode = DebugMode.Off,
    _relaxStrategy: ReductionStrategy[T] = CostBased[T](DefaultStateRanking[T]()),
    _restrictStrategy: ReductionStrategy[T] = CostBased[T](DefaultStateRanking[T]()),
    _stateDistance: StateDistance[T] = new StateDistance[T] {
      override def distance(t: T, t1: T): Double = 0.0
    }
  ): Solver = {
    val model: DdoModel[T] = new DdoModel[T] {

      override def problem(): Problem[T] = _problem

      override def relaxation(): Relaxation[T] = _relaxation

      override def ranking(): StateRanking[T] = _ranking

      override def widthHeuristic(): WidthHeuristic[T] = _widthHeuristic

      override def frontier(): Frontier[T] = {
        _frontier match {
          case CutSetType.LastExactLayer => SimpleFrontier.lastExactLayer[T](ranking())
          case CutSetType.Frontier       => SimpleFrontier[T](ranking())
        }
      }

      override def useCache(): Boolean = _useCache

      override def exportDot(): Boolean = _exportDot

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava

      override def relaxStrategy(): ReductionStrategy[T] = _relaxStrategy

      override def restrictStrategy(): ReductionStrategy[T] = _restrictStrategy

      override def stateDistance(): StateDistance[T] = _stateDistance
    }

    new Solver(new org.ddolib.ddo.core.solver.SequentialSolver[T](model))
  }
}
