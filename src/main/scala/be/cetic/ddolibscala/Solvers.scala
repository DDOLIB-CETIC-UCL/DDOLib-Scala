package be.cetic.ddolibscala

import be.cetic.ddolibscala.common.frontier.CutSetType
import be.cetic.ddolibscala.common.heuristics.width.FixedWidth
import be.cetic.ddolibscala.common.util.VerbosityLvl.Silent
import be.cetic.ddolibscala.common.util.{DebugMode, VerbosityLvl}
import be.cetic.ddolibscala.layered.heuristics.cluster.CostBased
import be.cetic.ddolibscala.layered.heuristics.variable.DefaultVariableHeuristic
import be.cetic.ddolibscala.layered.modeling.DefaultDominanceChecker
import org.ddolib.common.heuristics.width.WidthHeuristic
import org.ddolib.layered.modeling.{DominanceChecker, Relaxation, StateRanking}
import org.ddolib.layered.solving.ddo.core.heuristics.cluster.{ReductionStrategy, StateDistance}
import org.ddolib.layered.solving.ddo.core.heuristics.variable.VariableHeuristic

// Explicit package aliases renamed for style consistency
import be.cetic.ddolibscala.layered.{modeling as layeredModeling, solver as layeredSolver}
import be.cetic.ddolibscala.nolayer.{modeling as noLayerModeling, solver as noLayerSolver}

/** Unified factory for all solvers */
object Solvers {

  object layered {

    /** Instantiates and returns a DDO solver.
      */
    def ddo[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      relaxation: Relaxation[T],
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      ranking: StateRanking[T] = new layeredModeling.DefaultStateRanking[T](),
      widthHeuristic: WidthHeuristic[T] = FixedWidth[T](10),
      frontier: CutSetType = CutSetType.LastExactLayer,
      useCache: Boolean = false,
      exportDot: Boolean = false,
      variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off
    ): layeredSolver.Solver = {
      layeredSolver.DdoSolver(
        problem,
        relaxation,
        lowerBound,
        upperBound,
        dominance,
        ranking,
        widthHeuristic,
        frontier,
        useCache,
        exportDot,
        variableHeuristic,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns an exact solver.
      */
    def exact[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off,
      exportDot: Boolean = false
    ): layeredSolver.Solver = {
      layeredSolver.ExactSolver(problem, lowerBound, dominance, verbosityLvl, debugMode, exportDot)
    }

    /** Instantiates and returns an A* solver.
      */
    def astar[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off
    ): layeredSolver.Solver = {
      layeredSolver.AstarSolver(
        problem,
        lowerBound,
        upperBound,
        dominance,
        variableHeuristic,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns an Anytime column search (ACS) solver.
      */
    def acs[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      columnWidth: Int = 5,
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
      verbosityLvl: VerbosityLvl = Silent,
      debugMode: DebugMode = DebugMode.Off
    ): layeredSolver.Solver = {
      layeredSolver.AcsSolver(
        problem,
        columnWidth,
        lowerBound,
        upperBound,
        dominance,
        variableHeuristic,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns an Anytime Weighted A* solver.
      */
    def awastar[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      weight: Double = 5,
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
      verbosityLvl: VerbosityLvl = Silent,
      debugMode: DebugMode = DebugMode.Off
    ): layeredSolver.Solver = {
      layeredSolver.AwAstarSolver(
        problem,
        weight,
        lowerBound,
        upperBound,
        dominance,
        variableHeuristic,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns a LNS solver.
      */
    def lns[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
      verbosityLvl: VerbosityLvl = Silent,
      debugMode: DebugMode = DebugMode.Off,
      ranking: StateRanking[T] = new layeredModeling.DefaultStateRanking[T](),
      widthHeuristic: WidthHeuristic[T] = FixedWidth[T](10),
      exportDot: Boolean = false,
      restrictStrategy: ReductionStrategy[T] =
        CostBased[T](new layeredModeling.DefaultStateRanking[T]()),
      stateDistance: StateDistance[T] = new StateDistance[T] {
        override def distance(t: T, t1: T): Double = 0.0
      },
      initialSolution: Array[Int] = Array.empty[Int],
      probability: Double = 0.2,
      useLNS: Boolean = true
    ): layeredSolver.Solver = {
      layeredSolver.LnsSolver(
        problem,
        lowerBound,
        upperBound,
        dominance,
        variableHeuristic,
        verbosityLvl,
        debugMode,
        ranking,
        widthHeuristic,
        exportDot,
        restrictStrategy,
        stateDistance,
        initialSolution,
        probability,
        useLNS
      )
    }
  }

  object nolayer {

    /** Instantiates and returns a NoLayer A* solver.
      */
    def astar[T](
      problem: noLayerModeling.Problem[T],
      lowerBound: noLayerModeling.FastLowerBound[T] =
        new noLayerModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: noLayerModeling.NoLayerDominanceChecker[T] =
        new noLayerModeling.DefaultNoLayerDominanceChecker[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off
    ): noLayerSolver.Solver = {
      noLayerSolver.AstarSolver(
        problem,
        lowerBound,
        upperBound,
        dominance,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns a NoLayer Anytime Weighted A* solver.
      */
    def awastar[T](
      problem: noLayerModeling.Problem[T],
      weight: Double = 5,
      lowerBound: noLayerModeling.FastLowerBound[T] =
        new noLayerModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: noLayerModeling.NoLayerDominanceChecker[T] =
        new noLayerModeling.DefaultNoLayerDominanceChecker[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off
    ): noLayerSolver.Solver = {
      noLayerSolver.AwAstarSolver(
        problem,
        weight,
        lowerBound,
        upperBound,
        dominance,
        verbosityLvl,
        debugMode
      )
    }

    /** Instantiates and returns a NoLayer Anytime Column Search (ACS) solver.
      */
    def acs[T](
      problem: noLayerModeling.Problem[T],
      columnWidth: Int = 5,
      lowerBound: noLayerModeling.FastLowerBound[T] =
        new noLayerModeling.DefaultFastLowerBound[T](),
      upperBound: Double = Double.PositiveInfinity,
      dominance: noLayerModeling.NoLayerDominanceChecker[T] =
        new noLayerModeling.DefaultNoLayerDominanceChecker[T](),
      verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
      debugMode: DebugMode = DebugMode.Off
    ): noLayerSolver.Solver = {
      noLayerSolver.AcsSolver(
        problem,
        columnWidth,
        lowerBound,
        upperBound,
        dominance,
        verbosityLvl,
        debugMode
      )
    }
  }

}
