package org

package object ddolibscala {

  type Solver          = org.ddolib.common.solver.Solver
  type Solution        = org.ddolib.common.solver.Solution
  type SearchStatistic = org.ddolib.common.solver.SearchStatistics

  type FrontierType = org.ddolib.ddo.core.frontier.CutSetType
  object FrontierType {
    val LastExactLayer = org.ddolib.ddo.core.frontier.CutSetType.LastExactLayer
    val Frontier       = org.ddolib.ddo.core.frontier.CutSetType.Frontier
  }

}
