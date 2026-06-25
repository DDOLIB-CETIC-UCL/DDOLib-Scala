package be.cetic.ddolibscala.solver.nolayer

import be.cetic.ddolibscala.modeling.nolayer.*
import be.cetic.ddolibscala.solver.Solver
import be.cetic.ddolibscala.util.{DebugMode, VerbosityLvl}
import org.ddolib.modeling.nolayer.AcsModel
import org.ddolib.util.debug.DebugLevel
import org.ddolib.util.verbosity.VerbosityLevel

/** Defines factory for a NoLayer
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/acs/core/solver/nolayer/AcsSolver.html Anytime Column Search (ACS) solver]]
  */
private[solver] object AcsSolver {

  /** Instantiates and returns an
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/acs/core/solver/nolayer/AcsSolver.html Anytime column search (ACS) solver]]
    * operating without layer or fixed variable concepts.
    *
    * @param _problem
    *   the structure defining the transitions and objective function of the optimization task
    * @param _columnWidth
    *   column width used for formatted output during the Anytime Column Search process
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
    *   a solver based on the ACS algorithm without layers
    */
  def apply[T](
    _problem: Problem[T],
    _columnWidth: Int = 5,
    _lowerBound: FastLowerBound[T] = DefaultFastLowerBound[T](),
    _upperBound: Double = Double.PositiveInfinity,
    _dominance: NoLayerDominanceChecker[T] = DefaultNoLayerDominanceChecker[T](),
    _verbosityLvl: VerbosityLvl = VerbosityLvl.Silent,
    _debugMode: DebugMode = DebugMode.Off
  ): Solver = {

    val model: AcsModel[T] = new AcsModel[T] {
      override def problem(): org.ddolib.modeling.nolayer.Problem[T] = _problem

      override def columnWidth(): Int = _columnWidth

      override def lowerBound(): org.ddolib.modeling.nolayer.FastLowerBound[T] = _lowerBound

      override def upperBound(): Double = _upperBound

      override def dominance(): org.ddolib.common.dominance.NoLayerDominanceChecker[T] = _dominance

      override def verbosityLevel(): VerbosityLevel = _verbosityLvl.toJava

      override def debugMode(): DebugLevel = _debugMode.toJava
    }

    new Solver(new org.ddolib.solving.acs.core.solver.nolayer.AcsSolver[T](model))
  }
}
