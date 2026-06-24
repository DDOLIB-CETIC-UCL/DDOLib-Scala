package be.cetic.ddolibscala.example.tsptw

import be.cetic.ddolibscala.example.tsptw.layered.{TspTwDominance, TspTwFlb, TspTwProblem, TspTwRanking, TspTwRelax, TspTwState}
import be.cetic.ddolibscala.modeling.layered.{FastLowerBound, Relaxation, StateRanking}
import be.cetic.ddolibscala.tools.dominance.SimpleDominanceChecker
import org.ddolib.common.dominance.DominanceChecker
import be.cetic.ddolibscala.util.testbench.{ProblemLoader, ProblemTestBench, TestModel}
import org.scalatest.funsuite.AnyFunSuite

class TspTwTest extends AnyFunSuite {

  val problems: List[TspTwProblem] =
    ProblemLoader.loadFromDir("src/test/resources/TSPTW")(TspTwProblem(_))

  val configFactory: TspTwProblem => TestModel[TspTwState] = (problem: TspTwProblem) =>
    new TestModel[TspTwState] {
      override def flb: FastLowerBound[TspTwState] = TspTwFlb(problem)

      override def relaxation: Option[Relaxation[TspTwState]] = Some(TspTwRelax(problem.nbVars()))

      override def ranking: StateRanking[TspTwState] = TspTwRanking()

      override def dominance: DominanceChecker[TspTwState] =
        SimpleDominanceChecker(TspTwDominance(), problem.nbVars())
    }

  val bench = ProblemTestBench(problems, configFactory, bestSolutionKnown = true)
  bench.minWidth = 15

  bench.generateTests().foreach { testCase =>
    test(testCase.name) {
      testCase.executeTest()
    }
  }

}
