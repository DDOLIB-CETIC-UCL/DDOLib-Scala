package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.layered.modeling.DefaultFastLowerBound
import be.cetic.ddolibscala.layered.heuristics.variable.DefaultVariableHeuristic
import be.cetic.ddolibscala.layered.modeling.DefaultDominanceChecker
import be.cetic.ddolibscala.common.util.VerbosityLvl.Silent
import be.cetic.ddolibscala.common.util.{DebugMode, VerbosityLvl}
import org.ddolib.layered.modeling.DominanceChecker
import org.ddolib.layered.modeling.{AcsModel, FastLowerBound, Problem}
import org.ddolib.layered.solving.ddo.core.heuristics.variable.VariableHeuristic
import org.ddolib.common.util.debug.DebugLevel
import org.ddolib.common.util.verbosity.VerbosityLevel

/** Defines factory for an
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/acs/core/solver/AcsSolver.html Anytime Column Search (ACS) solver]]
  */
private[ddolibscala] object AcsSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/acs/core/solver/AcsSolver.html Anytime column search (ACS) solver]]
    *
    * @param _problem
    *   the structure defining the structure, transitions, and objective function of the
    *   optimization task
    * @param _columnWidth
    *   column width used for formatted output during the Anytime Column Search process.
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
    *   a solver based on the ACS algorithm
    */
  def apply[T](
    _problem: Problem[T],
    _columnWidth: Int = 5,
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double = Double.PositiveInfinity,
    _dominance: DominanceChecker[T] = new DefaultDominanceChecker[T](),
    _variableHeuristic: VariableHeuristic[T] = DefaultVariableHeuristic[T](),
    _verbosityLvl: VerbosityLvl = Silent,
    _debugMode: DebugMode = DebugMode.Off
  ): Solver = {

    val model: AcsModel[T] = new AcsModel[T] {
      override def problem(): Problem[T] = _problem

      override def columnWidth(): Int = _columnWidth

      override def lowerBound(): FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): DominanceChecker[T] = _dominance

      override def variableHeuristic(): VariableHeuristic[T] = _variableHeuristic

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.layered.solving.acs.core.solver.AcsSolver[T](model))
  }
}
