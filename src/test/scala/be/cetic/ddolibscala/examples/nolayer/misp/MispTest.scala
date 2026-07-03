package be.cetic.ddolibscala.examples.nolayer.misp

import be.cetic.ddolibscala.nolayer.modeling.{FastLowerBound, NoLayerDominanceChecker}
import be.cetic.ddolibscala.util.testbench.ProblemLoader
import be.cetic.ddolibscala.nolayer.util.testbench.{ProblemTestBench, TestModel}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.BitSet

class MispTest extends AnyFunSuite {
  val problems: List[MispProblem] =
    ProblemLoader.loadFromDir("src/test/resources/MISP")(MispProblem(_))

  val configFactory: MispProblem => TestModel[BitSet] = (problem: MispProblem) => {
    new TestModel[BitSet] {
      override def flb: FastLowerBound[BitSet] = MispFlb(problem)

      override def dominance: NoLayerDominanceChecker[BitSet] = MispNoLayerDominance(
        problem.numNodes
      )
    }
  }

  val bench = ProblemTestBench(problems, configFactory, bestSolutionKnown = false)

  bench.generateTests().foreach { testCase =>
    test(testCase.name) {
      testCase.executeTest()
    }
  }

}
