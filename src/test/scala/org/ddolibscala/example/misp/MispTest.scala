package org.ddolibscala.example.misp

import org.ddolib.common.dominance.DominanceChecker
import org.ddolibscala.modeling.{FastLowerBound, Relaxation, StateRanking}
import org.ddolibscala.tools.dominance.SimpleDominanceChecker
import org.ddolibscala.util.testbench.{ProblemLoader, ProblemTestBench, TestModel}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.BitSet

class MispTest extends AnyFunSuite {

  val problems: List[MispProblem] =
    ProblemLoader.loadFromDir("src/test/resources/MISP")(MispProblem(_))

  val configFactory: MispProblem => TestModel[BitSet] = (problem: MispProblem) =>
    new TestModel[BitSet] {
      override def flb: FastLowerBound[BitSet]            = MispFlb(problem)
      override def relaxation: Option[Relaxation[BitSet]] = Some(MispRelaxation())
      override def ranking: StateRanking[BitSet]          = MispRanking()
      override def dominance: DominanceChecker[BitSet]    =
        SimpleDominanceChecker(MispDominance(), problem.nbVars())
    }

  val bench = ProblemTestBench(problems, configFactory)

  bench.generateTests().foreach { testCase =>
    test(testCase.name) {
      testCase.executeTest()
    }
  }
}
