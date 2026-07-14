package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.layered.modeling.{DefaultFastLowerBound, DefaultStateRanking}
import be.cetic.ddolibscala.layered.heuristics.cluster.CostBased
import be.cetic.ddolibscala.layered.heuristics.variable.DefaultVariableHeuristic
import be.cetic.ddolibscala.common.heuristics.width.FixedWidth
import be.cetic.ddolibscala.layered.modeling.DefaultDominanceChecker
import be.cetic.ddolibscala.common.util.VerbosityLvl.Silent
import be.cetic.ddolibscala.common.util.{DebugMode, VerbosityLvl}
import org.ddolib.layered.modeling.DominanceChecker
import org.ddolib.layered.modeling.{FastLowerBound, LnsModel, Problem, StateRanking}
import org.ddolib.layered.solving.ddo.core.heuristics.cluster.{ReductionStrategy, StateDistance}
import org.ddolib.layered.solving.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.common.heuristics.width.WidthHeuristic
import org.ddolib.common.util.debug.DebugLevel
import org.ddolib.common.util.verbosity.VerbosityLevel

/** Defines factory for a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/lns/core/solver/LnsSolver.html LNS solver]].
  */
private[ddolibscala] object LnsSolver {

  /** Instantiates and returns a
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/lns/core/solver/LnsSolver.html LNS solver]].
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
    * @param _ranking
    *   the heuristic used to rank states
    * @param _widthHeuristic
    *   heuristic controlling the maximum number of nodes per layer
    * @param _exportDot
    *   whether the generated diagram must be exported to DOT file
    * @param _restrictStrategy
    *   strategy to select which nodes should be dropped on a restricted DD
    * @param _stateDistance
    *   distance function between states, used to form clusters
    * @param _initialSolution
    *   an initial solution to start the LNS search
    * @param _probability
    *   the probability used to destruct parts of the solution in LNS (`0.2` by default)
    * @param _useLNS
    *   whether LNS should be used
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver based on the LNS algorithm
    */
  def apply[T](
    _problem: Problem[T],
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double = Double.PositiveInfinity,
    _dominance: DominanceChecker[T] = new DefaultDominanceChecker[T](),
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLvl = Silent,
    _debugMode: DebugMode = DebugMode.Off,
    _ranking: StateRanking[T] = DefaultStateRanking[T](),
    _widthHeuristic: WidthHeuristic[T] = FixedWidth[T](10),
    _exportDot: Boolean = false,
    _restrictStrategy: ReductionStrategy[T] = CostBased[T](DefaultStateRanking[T]()),
    _stateDistance: StateDistance[T] = new StateDistance[T] {
      override def distance(t: T, t1: T): Double = 0.0
    },
    _initialSolution: Array[Int] = Array.empty[Int],
    _probability: Double = 0.2,
    _useLNS: Boolean = true
  ): Solver = {

    val model: LnsModel[T] = new LnsModel[T] {
      override def problem(): Problem[T] = _problem

      override def ranking(): StateRanking[T] = _ranking

      override def widthHeuristic(): WidthHeuristic[T] = _widthHeuristic

      override def exportDot(): Boolean = _exportDot

      override def restrictStrategy(): ReductionStrategy[T] = _restrictStrategy

      override def stateDistance(): StateDistance[T] = _stateDistance

      override def initialSolution(): Array[Int] = _initialSolution

      override def probability(): Double = _probability

      override def useLNS(): Boolean = _useLNS

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    Solver(new org.ddolib.layered.solving.lns.core.solver.LnsSolver[T](model))
  }

}
