package org

import org.ddolibscala.solver._

package object ddolibscala {

  type Solver          = org.ddolibscala.solver.Solver
  type Solution        = org.ddolib.common.solver.Solution
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

  object DdoSolver   extends DdoSolver
  object ExactSolver extends ExactSolver
  object AstarSolver extends AstarSolver
  object AcsSolver   extends AcsSolver
}
