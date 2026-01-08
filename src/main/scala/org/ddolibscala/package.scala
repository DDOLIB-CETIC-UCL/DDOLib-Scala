package org

import org.ddolibscala.solver.*

/** Main package of the framework. It contains traits that bridge between Scala and the Java library
  * [[https://github.com/DDOLIB-CETIC-UCL/DDOLib DDOLib]]. It also defines a fully Scala user API.
  */
package object ddolibscala {

  /** Alias for the [[org.ddolibscala.solver.Solver]] type. */
  type Solver = org.ddolibscala.solver.Solver

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/Solution.html org.ddolib.common.solver.Solution]]
    * object.
    */
  type Solution = org.ddolib.common.solver.Solution

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/SearchStatistics.html org.ddolib.common.solver.SearchStatistics]]
    */
  type SearchStatistic = org.ddolib.common.solver.SearchStatistics

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/frontier/CutSetType.html org.ddolib.ddo.core.frontier.CutSetType]]
    */
  type CutSetType = org.ddolib.ddo.core.frontier.CutSetType

  /** Defines shortcuts for each cutset types. */
  object CutSetType {

    /** The cut set corresponds to the last layer of the search space that can be evaluated exactly
      * before approximations are applied.
      */
    val LastExactLayer = org.ddolib.ddo.core.frontier.CutSetType.LastExactLayer

    /** The cut set is defined by the current search frontier — the set of nodes that are candidates
      * for expansion.
      */
    val Frontier = org.ddolib.ddo.core.frontier.CutSetType.Frontier
  }

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/util/verbosity/VerbosityLevel.html org.ddolib.util.verbosity.VerbosityLevel]]
    */
  type VerbosityLevel = org.ddolib.util.verbosity.VerbosityLevel

  /** Defines shortcuts for each verbosity level */
  object VerbosityLevel {

    /** No output is produced.
      *
      * <p> This mode is intended for fully silent execution, where performance measurements or logs
      * are not required. </p>
      */
    val SILENT = org.ddolib.util.verbosity.VerbosityLevel.SILENT

    /** Displays important progress updates. <p> In this mode, the solver prints a message each time
      * a new best objective value is found during the search. </p>
      */
    val NORMAL = org.ddolib.util.verbosity.VerbosityLevel.NORMAL

    /** Displays detailed runtime information for debugging or analysis purposes.
      *
      * <p> In this mode, the solver outputs:
      *   - A message whenever a new best objective is found.
      *   - Periodic statistics about the search frontier (approximately every 0.5 seconds)
      *   - Information about each developed subproblem as the search progresses </p>
      *
      * This mode provides the highest level of detail and is useful for performance analysis and
      * algorithmic tuning.
      */
    val LARGE = org.ddolib.util.verbosity.VerbosityLevel.LARGE

    /** Same that [[LARGE]] but saves the logs into `logs.txt` file. */
    val EXPORT = org.ddolib.util.verbosity.VerbosityLevel.EXPORT
  }

  /** Alias for
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/util/debug/DebugLevel.html org.ddolib.util.debug.DebugLevel]]
    */
  type DebugMode = org.ddolib.util.debug.DebugLevel

  /** Defines shortcuts  for each debug mode. */
  object DebugMode {

    /** Disables all debugging features. <p> This mode is intended for production runs or
      * benchmarking, where performance is prioritized and no additional checks or debug information
      * are generated. </p>
      */
    val OFF = org.ddolib.util.debug.DebugLevel.OFF

    /** Enables basic debugging checks. <p> This mode verifies fundamental properties of the model
      * components, such as: </p> <ul> <li>Equality and correctness of state representations.</li>
      * <li>Proper definition and consistency of lower bounds.</li> </ul> These checks help detect
      * common modeling or implementation errors early in the solving process.
      */
    val ON = org.ddolib.util.debug.DebugLevel.ON

    /** Enables extended debugging and diagnostic tools.
      *
      * <p> Includes all checks from `ON`, and adds: </p>
      *   - Export of failing or inconsistent decision diagrams (as <code>.dot</code> files) to
      *     assist with visualization and analysis.
      *   - Additional consistency verification of lower-bound computations (particularly useful for
      *     A*-based algorithms)
      *
      * This mode is recommended when investigating unexpected solver behavior or validating complex
      * model implementations.
      */
    val EXTENDED = org.ddolib.util.debug.DebugLevel.EXTENDED
  }

  /** Instantiates a solver based on the DDO algorithm */
  object DdoSolver extends DdoSolver

  /** Instantiates a solver that generates a complete decision diagram solve a given problem.
    *
    * @note
    *   Should be used on small instances for debug purpose.
    */
  object ExactSolver extends ExactSolver

  /** Instantiates a solver based on the A* algorithm. */
  object AstarSolver extends AstarSolver

  /** Instantiates a solver based on the Anytime Column Search algorithm. */
  object AcsSolver extends AcsSolver
}
