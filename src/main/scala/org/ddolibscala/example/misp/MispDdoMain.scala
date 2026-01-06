package org.ddolibscala.example.misp

import org.ddolib.common.dominance.{DominanceChecker, SimpleDominanceChecker}
import org.ddolib.common.solver.Solution
import org.ddolib.ddo.core.frontier.{CutSetType, Frontier, SimpleFrontier}
import org.ddolib.ddo.core.heuristics.width.{FixedWidth, WidthHeuristic}
import org.ddolib.modeling._

object MispDdoMain {

  def main(args: Array[String]): Unit = {
    val p = MispProblem("data/MISP/weighted.dot")
    println(p)

    val model: DdoModel[Set[Int]] = new DdoModel[Set[Int]] {
      override def problem(): Problem[Set[Int]] = p

      override def relaxation(): Relaxation[Set[Int]] = MispRelaxation(p)

      override def ranking(): StateRanking[Set[Int]] = MispRanking()

      override def lowerBound(): FastLowerBound[Set[Int]] = MispFlb(p)

      override def widthHeuristic(): WidthHeuristic[Set[Int]] = new FixedWidth[Set[Int]](2)

      override def frontier(): Frontier[Set[Int]] =
        new SimpleFrontier[Set[Int]](ranking(), CutSetType.Frontier)

      override def useCache(): Boolean = true

      override def dominance(): DominanceChecker[Set[Int]] =
        new SimpleDominanceChecker[Set[Int]](MispDominance(), p.nbVars())

      override def exportDot(): Boolean = true
    }

    val solution: Solution = Solvers.minimizeDdo(model)

    println(solution)

  }

}
