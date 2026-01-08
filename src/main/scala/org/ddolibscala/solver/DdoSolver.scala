package org.ddolibscala
package solver

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.ddo.core.frontier.Frontier
import org.ddolib.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling.*
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel
import org.ddolibscala.modeling.{DefaultFastLowerBound, DefaultStateRanking}
import org.ddolibscala.tools.ddo.frontier.CutSetType.LastExactLayer
import org.ddolibscala.tools.ddo.frontier.{CutSetType, SimpleFrontier}
import org.ddolibscala.tools.ddo.heuristics.variables.DefaultVariableHeuristic
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.DefaultDominanceChecker
import org.ddolibscala.util.{DebugMode, VerbosityLvl}

/** Defines factory for a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/solver/SequentialSolver.html DDO solver]].
  */
object DdoSolver {

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
  def apply[T](
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
    verbosityLvl: VerbosityLvl = VerbosityLvl.SILENT,
    debugMode: DebugMode = DebugMode.OFF
  ): Solver = {
    initSolver(
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

  /** Internal method that initialize the solver allowing simpler parameters' name in the `apply`
    * method.
    */
  private def initSolver[T](
    _problem: Problem[T],
    _relaxation: Relaxation[T],
    _lowerBound: FastLowerBound[T],
    _dominance: DominanceChecker[T],
    _ranking: StateRanking[T],
    _widthHeuristic: WidthHeuristic[T],
    _frontier: CutSetType,
    _useCache: Boolean,
    _exportDot: Boolean,
    _variableHeuristic: VariableHeuristic[T],
    _verbosityLvl: VerbosityLvl,
    _debugMode: DebugMode
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

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.ddo.core.solver.SequentialSolver[T](model))
  }

}
