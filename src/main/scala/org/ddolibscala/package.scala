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

  type FrontierType = org.ddolib.ddo.core.frontier.CutSetType
  object FrontierType {
    val LastExactLayer = org.ddolib.ddo.core.frontier.CutSetType.LastExactLayer
    val Frontier       = org.ddolib.ddo.core.frontier.CutSetType.Frontier
  }

  type VerbosityLevel = org.ddolib.util.verbosity.VerbosityLevel
  object VerbosityLevel {
    val SILENT = org.ddolib.util.verbosity.VerbosityLevel.SILENT
    val NORMAL = org.ddolib.util.verbosity.VerbosityLevel.NORMAL
    val LARGE  = org.ddolib.util.verbosity.VerbosityLevel.LARGE
    val EXPORT = org.ddolib.util.verbosity.VerbosityLevel.EXPORT
  }

  type DebugMode = org.ddolib.util.debug.DebugLevel
  object DebugMode {
    val OFF      = org.ddolib.util.debug.DebugLevel.OFF
    val ON       = org.ddolib.util.debug.DebugLevel.ON
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
