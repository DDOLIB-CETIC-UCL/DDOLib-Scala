package be.cetic.ddolibscala.example.misp

import be.cetic.ddolibscala.example.misp.layered.{MispDominance, MispFlb, MispProblem, MispRanking, MispRelaxation}
import be.cetic.ddolibscala.modeling.layered.{FastLowerBound, Relaxation, StateRanking}
import be.cetic.ddolibscala.tools.dominance.SimpleDominanceChecker
import be.cetic.ddolibscala.util.testbench.{ProblemLoader, ProblemTestBench, TestModel}
import org.ddolib.common.dominance.DominanceChecker
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

  val bench = ProblemTestBench(problems, configFactory, bestSolutionKnown = true)

  bench.generateTests().foreach { testCase =>
    test(testCase.name) {
      testCase.executeTest()
    }
  }
}
