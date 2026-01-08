package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling.{FastLowerBound, Problem, Relaxation, StateRanking}
import org.ddolibscala.modeling.{DefaultFastLowerBound, DefaultStateRanking}
import org.ddolibscala.tools.ddo.frontier.CutSetType
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.VerbosityLvl.Silent
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

/** Factory for solvers */
object Solver {

  /** Instantiates and returns a
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/SequentialSolver.html DDO solver]].
    *
    * @param problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param relaxation
    *   the relaxation of the model used to evaluate the nodes or layers of the decision diagram.
    * @param lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param ranking
    *   the heuristic used to determine the next variable to branch on during decision diagram
    *   compilation
    * @param widthHeuristic
    *   heuristic controlling the maximum number of nodes per layer
    * @param frontier
    *   type of frontier management strategy used to store and expand the current layer of the
    *   decision diagram.
    * @param useCache
    *   whether caching mechanism must be used
    * @param exportDot
    *   whether the generated diagram must be exported to DOT file
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
    *   a solver based on the DDO algorithm
    */
  def ddo[T](
              problem: Problem[T],
              relaxation: Relaxation[T],
              lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
              dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
              ranking: StateRanking[T] = DefaultStateRanking[T](),
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

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/ExactSolver.html exact solver]]
    *
    * @param problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param lowerBound
    *   a heuristic that estimates a lower bound on the objective value for a given state
    * @param dominance
    *   the dominance checker used to prune dominated states from the search space
    * @param verbosityLvl
    *   the verbosity level of the solver when this model is executed
    * @param debugMode
    *   the debugging level to apply during the compilation and solving phases
    * @param exportDot
    *   whether the generated diagram must be exported to DOT file
    * @tparam T
    *   the type representing a state in the problem
    * @return
    *   a solver that generate a complete decision diagram to solve the problem
    */
  def exact[T](
                problem: Problem[T],
                lowerBound: FastLowerBound[T] = DefaultFastLowerBound(),
                dominance: DominanceChecker[T] = DefaultDominanceChecker(),
                verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
                debugMode: DebugMode = DebugMode.Off,
                exportDot: Boolean = false
  ): Solver = {
    ExactSolver(problem, lowerBound, dominance, verbosityLvl, debugMode, exportDot)

  }

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
  def astar[T](
                problem: Problem[T],
                lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
                dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
                variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
                verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
                debugMode: DebugMode = DebugMode.Off
  ): Solver = {
    AstarSolver(problem, lowerBound, dominance, variableHeuristic, verbosityLvl, debugMode)
  }

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
  def acs[T](
              problem: Problem[T],
              columnWidth: Int = 5,
              lowerBound: FastLowerBound[T] = DefaultFastLowerBound(),
              dominance: DominanceChecker[T] = DefaultDominanceChecker(),
              variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic(),
              verbosityLvl: VerbosityLvl = Silent,
              debugMode: DebugMode = DebugMode.Off
  ): Solver = {
    AcsSolver(
      problem,
      columnWidth,
      lowerBound,
      dominance,
      variableHeuristic,
      verbosityLvl,
      debugMode
    )
  }

}

/** Class packaging a Java solver
  *
  * @param javaSolver
  *   a solver from java version of DDOLib
  */
class Solver(javaSolver: org.ddolib.common.solver.Solver) {

  /** Minimizes the objective function according to the solver strategy.
    *
    * <p> It converts input and output from Java to Scala and vice versa </p>
    *
    * @param limit
    *   a predicate that can limit or stop the search based on current
    *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/SearchStatistics.html SearchStatistics]]
    * @param onSolution
    *   invoked on each new solution found; receives the solution array and current statistics
    * @return
    *   the solution obtained after the search
    */
  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Array[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    javaSolver.minimize(limit.asJava, onSolution.asJava)
  }

}
