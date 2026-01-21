package org.ddolibscala.example.misp

import org.ddolib.common.dominance.DominanceChecker
import org.ddolibscala.modeling.{FastLowerBound, Relaxation, StateRanking}
import org.ddolibscala.tools.dominance.SimpleDominanceChecker
import org.ddolibscala.util.testbench.{ProblemTestBench, SolverTestConfig}

import scala.collection.immutable.BitSet

class MispTest extends ProblemTestBench[BitSet, MispProblem] {

  override protected def generateProblem(): List[MispProblem] = {
    loadProblemsFromDir("src/test/resources/MISP")(MispProblem(_))
  }

  override protected def solverConfig(problem: MispProblem): SolverTestConfig[BitSet] = {
    new SolverTestConfig[BitSet]() {

      override def flb: FastLowerBound[BitSet] = MispFlb(problem)

      override def relaxation: Option[Relaxation[BitSet]] = Some(MispRelaxation())

      override def ranking: StateRanking[BitSet] = MispRanking()

      override def dominance: DominanceChecker[BitSet] =
        SimpleDominanceChecker(MispDominance(), problem.nbVars())
    }
  }
}
