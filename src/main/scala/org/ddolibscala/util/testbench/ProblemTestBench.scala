package org.ddolibscala
package util.testbench

import org.ddolibscala.modeling.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.{Try, Using}

abstract class ProblemTestBench[T, P <: Problem[T]](pathToData: String)
    extends AnyFunSuite
    with Matchers {

  private val dataFiles: List[String] = {
    val path                        = Paths.get(pathToData)
    val toReturn: Try[List[String]] = Using(Files.list(path)) { stream =>
      stream.filter(Files.isRegularFile(_)).map(_.toString).toList.asScala.toList
    }

    toReturn.getOrElse(Nil)
  }

  private val problems: List[P] = generateProblem()

  protected def generateProblem(): List[P]

  protected def model(problem: Problem[T]): TestModel[T, P]

  private def testSolution(bestSolution: Solution, problem: P, width: Int = -1): Unit = {
    val bestValue                       = bestSolution.value()
    val optBestVal: Option[Double]      = if (bestValue.isInfinite) None else Some(bestValue)
    val expectedOptimal: Option[Double] = problem.optimal
    withClue({ if (width != -1) s"Max width of MDD: $width" else "" }) {
      if (optBestVal.isDefined && expectedOptimal.isDefined) {
        optBestVal.get must ===(expectedOptimal.get +- 1e-10)
      } else {
        optBestVal must equal(expectedOptimal)
      }

      if (expectedOptimal.isDefined) {
        problem.evaluate(bestSolution.solution()) must ===(expectedOptimal.get +- 1e-10)
      }

    }
  }
}
