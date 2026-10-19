package be.cetic.ddolibscala.common.solver.stopcriterion

import org.ddolib.common.solver.stat.{AstarStats, DdoStats}
import org.scalatest.funsuite.AnyFunSuite

class StopCriterionTest extends AnyFunSuite {

  private def ddoStats(nbIter: Int, nbNodes: Int): DdoStats = {
    var stats = new DdoStats(System.currentTimeMillis(), Double.PositiveInfinity)
    for (_ <- 0 until nbIter) {
      stats = stats.incrementNbIter()
    }
    stats.addNodes(nbNodes)
  }

  test("maxIter stops once the limit is reached") {
    assert(!StopCriterion.maxIter(5)(ddoStats(4, 0)))
    assert(StopCriterion.maxIter(5)(ddoStats(5, 0)))
  }

  test("DDO criterion is evaluated on DDO statistics") {
    assert(!DdoStopCriterion.maxTotalMddNodes(10)(ddoStats(0, 9)))
    assert(DdoStopCriterion.maxTotalMddNodes(10)(ddoStats(0, 10)))
  }

  test("combinators mix generic and specific criteria") {
    val iter  = StopCriterion.maxIter(5)
    val nodes = DdoStopCriterion.maxTotalMddNodes(10)

    assert(!(iter && nodes)(ddoStats(5, 0)))
    assert((iter && nodes)(ddoStats(5, 10)))
    assert(!(iter || nodes)(ddoStats(0, 0)))
    assert((iter || nodes)(ddoStats(0, 10)))
    assert((!iter)(ddoStats(0, 0)))
  }

  test("DDO criterion rejects A* statistics") {
    val astarStats = new AstarStats(System.currentTimeMillis(), Double.PositiveInfinity)
    assertThrows[IllegalArgumentException] {
      DdoStopCriterion.maxExploredDepth(3)(astarStats)
    }
  }
}
