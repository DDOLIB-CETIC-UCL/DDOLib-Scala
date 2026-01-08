package org.ddolibscala.example.misp

import org.ddolib.common.dominance.DominanceChecker
import org.ddolib.common.solver.Solution
import org.ddolib.ddo.core.frontier.Frontier
import org.ddolib.ddo.core.heuristics.width.WidthHeuristic
import org.ddolib.modeling._
import org.ddolibscala.tools.ddo.frontier.SimpleFrontier
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.tools.dominance.SimpleDominanceChecker

import scala.collection.immutable.BitSet

object MispDdoMain {

  def main(args: Array[String]): Unit = {

    // Just an example to test if all is working. This is not the final user api

    val p = MispProblem("data/MISP/weighted.dot")
    println(p)

    val model: DdoModel[BitSet] = new DdoModel[BitSet] {
      override def problem(): Problem[BitSet] = p

      override def relaxation(): Relaxation[BitSet] = MispRelaxation()

      override def ranking(): StateRanking[BitSet] = MispRanking()

      override def lowerBound(): FastLowerBound[BitSet] = MispFlb(p)

      override def widthHeuristic(): WidthHeuristic[BitSet] = FixedWidth[BitSet](2)

      override def frontier(): Frontier[BitSet] =
        SimpleFrontier(ranking()) // or SimpleFrontier.lastExactLayer(ranking())

      override def useCache(): Boolean = true

      override def dominance(): DominanceChecker[BitSet] =
        SimpleDominanceChecker[BitSet](MispDominance(), p.nbVars())

      override def exportDot(): Boolean = true
    }

    val solution: Solution = Solvers.minimizeDdo(model)

    println(solution)

  }

}
