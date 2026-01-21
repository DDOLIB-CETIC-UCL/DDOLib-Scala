package org.ddolibscala.example.misp

import org.ddolib.common.dominance.DominanceChecker
import org.ddolibscala.modeling.{FastLowerBound, Relaxation, StateRanking}
import org.ddolibscala.tools.dominance.SimpleDominanceChecker
import org.ddolibscala.util.testbench.{ProblemTestBench, TestModel}

import java.nio.file.{Files, Paths}
import scala.collection.immutable.BitSet
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.{Try, Using}

class MispTest extends ProblemTestBench[BitSet, MispProblem] {

  override protected def generateProblem(): List[MispProblem] = {
    val pathToData                  = "src/test/resources/MISP"
    val path                        = Paths.get(pathToData)
    val allFiles: Try[List[String]] = Using(Files.list(path)) { stream =>
      stream.filter(Files.isRegularFile(_)).map(_.toString).toList.asScala.toList
    }

    allFiles.getOrElse(Nil).map(MispProblem(_))
  }

  override protected def model(p: MispProblem): TestModel[BitSet, MispProblem] = {
    new TestModel[BitSet, MispProblem]() {

      override def problem: MispProblem = p

      override def flb: FastLowerBound[BitSet] = MispFlb(p)

      override def relaxation: Option[Relaxation[BitSet]] = Some(MispRelaxation())

      override def ranking: StateRanking[BitSet] = MispRanking()

      override def dominance: DominanceChecker[BitSet] =
        SimpleDominanceChecker(MispDominance(), p.nbVars())
    }
  }
}
