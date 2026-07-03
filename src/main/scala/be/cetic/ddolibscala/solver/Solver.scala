package be.cetic.ddolibscala.solver

import be.cetic.ddolibscala.solver.layered.*
import be.cetic.ddolibscala.tools.ddo.frontier.CutSetType
import be.cetic.ddolibscala.tools.ddo.heuristics.cluster.layered.CostBased
import be.cetic.ddolibscala.tools.ddo.heuristics.variables.layered.DefaultVariableHeuristic
import be.cetic.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import be.cetic.ddolibscala.tools.dominance.DefaultDominanceChecker
import be.cetic.ddolibscala.util.VerbosityLvl.Silent
import be.cetic.ddolibscala.util.{DebugMode, VerbosityLvl}
import be.cetic.ddolibscala.{NoLayerSolution, SearchStatistic, Solution}
import org.ddolib.layered.modeling.DominanceChecker
import org.ddolib.layered.modeling.{Relaxation, StateRanking}
import org.ddolib.layered.solving.ddo.core.heuristics.cluster.{ReductionStrategy, StateDistance}
import org.ddolib.layered.solving.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.common.heuristics.width.WidthHeuristic

import scala.jdk.CollectionConverters.ListHasAsScala
import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

// Explicit package aliases renamed for style consistency
import be.cetic.ddolibscala.modeling.{layered as layeredModeling, nolayer as noLayerModeling}

/** Factory for solvers */
object Solver {

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
    ): Solver = {
      DdoSolver(
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
    ): Solver = {
      ExactSolver(problem, lowerBound, dominance, verbosityLvl, debugMode, exportDot)
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
    ): Solver = {
      AstarSolver(
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
    ): Solver = {
      AcsSolver(
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
    ): Solver = {
      AwAstarSolver(
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
    ): Solver = {
      LnsSolver(
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
    ): NoLayerSolver = {
      be.cetic.ddolibscala.solver.nolayer.AstarSolver(
        problem,
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
    ): NoLayerSolver = {
      be.cetic.ddolibscala.solver.nolayer.AcsSolver(
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

/** Class packaging a Java layered solver
  */
class Solver private[solver] (javaSolver: org.ddolib.layered.solver.Solver) {

  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Array[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    javaSolver.minimize(limit.asJava, onSolution.asJava)
  }

}

/** Class packaging a Java NoLayer solver
  */
class NoLayerSolver private[solver] (javaSolver: org.ddolib.nolayer.solver.Solver) {

  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Seq[Int], SearchStatistic) => Unit = (_, _) => {}
  ): NoLayerSolution = {
    val onSolutionJava = (solution: java.util.List[java.lang.Integer], stats: SearchStatistic) =>
      onSolution(solution.asScala.toSeq.map(_.toInt), stats)
    javaSolver.minimize(limit.asJava, onSolutionJava.asJava)
  }

}
