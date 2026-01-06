package org.ddolibscala.example.misp

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.common.solver.Solution
import org.ddolib.ddo.core.frontier.Frontier
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling._
import org.ddolibscala.common.dominance.SimpleDominanceChecker
import org.ddolibscala.ddo.core.frontier.Frontier
import org.ddolibscala.ddo.core.heuristics.width.FixedWidth

object MispDdoMain {

  def main(args: Array[String]): Unit = {
    val p = MispProblem("data/MISP/weighted.dot")
    println(p)

    val model: DdoModel[Set[Int]] = new DdoModel[Set[Int]] {
      override def problem(): Problem[Set[Int]] = p

      override def relaxation(): Relaxation[Set[Int]] = MispRelaxation(p)

      override def ranking(): StateRanking[Set[Int]] = MispRanking()

      override def lowerBound(): FastLowerBound[Set[Int]] = MispFlb(p)

      override def widthHeuristic(): WidthHeuristic[Set[Int]] = FixedWidth[Set[Int]](2)

      override def frontier(): Frontier[Set[Int]] = Frontier(ranking())

      override def useCache(): Boolean = true

      override def dominance(): DominanceChecker[Set[Int]] =
        SimpleDominanceChecker[Set[Int]](MispDominance(), p.nbVars())

      override def exportDot(): Boolean = true
    }

    val solution: Solution = Solvers.minimizeDdo(model)

    println(solution)

  }

}
