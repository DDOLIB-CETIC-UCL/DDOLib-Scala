package org.ddolibscala.example.misp

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Problem

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

object MispProblem {

  def apply(
    nodes: Set[Int],
    neighbors: Array[Set[Int]],
    weight: Array[Int],
    optimal: Option[Double] = None
  ): MispProblem = new MispProblem(nodes, neighbors, weight, optimal)

  def apply(fname: String): MispProblem = {
    val weight: ArrayBuffer[Int]   = ArrayBuffer()
    var neighbors: Array[Set[Int]] = Array()
    var opti: Option[Double]       = None
    var numNodes: Int              = 0

    Using(Source.fromFile(fname)) { source =>
      val (nodesLines, edgesLines) = source.getLines().span(!_.contains("--"))

      for (line <- nodesLines if line.trim.nonEmpty) {
        if (line.contains("optimal")) {
          val optiStr: String       = line.replace(";", "")
          val tokens: Array[String] = optiStr.split("=")
          opti = Some(tokens(1).toDouble)
        } else if (line.contains("weight")) {
          var w: String = line.trim.split(" ")(1)
          w = w.replace("[weight=", "").replace("];", "")
          weight += w.toInt
        } else {
          weight += 1
        }
      }

      numNodes = weight.length
      neighbors = new Array[Set[Int]](numNodes)

      for (line <- edgesLines.takeWhile(_ != "}") if line.trim.nonEmpty) {
        val tokens: Array[String] = line.trim.replace(" ", "").replace(";", "").split("--")
        val source: Int           = tokens(0).toInt - 1
        val target: Int           = tokens(1).toInt - 1
        neighbors(source) = neighbors(source) + target
        neighbors(target) = neighbors(target) + source
      }
    }

    MispProblem((0 until numNodes).toSet, neighbors, weight.toArray, opti)

  }
}

class MispProblem(
  nodes: Set[Int],
  val neighbors: Array[Set[Int]],
  val weight: Array[Int],
  _optimal: Option[Double]
) extends Problem[Set[Int]] {

  override def optimal: Option[Double] = _optimal

  override def nbVars(): Int = weight.length

  override def initialState(): Set[Int] = nodes

  override def initialValue(): Double = 0.0

  override def nextValues(state: Set[Int], variable: Int): Iterable[Int] = {
    if (state.contains(variable)) List(0, 1)
    else List(0)
  }

  override def transition(state: Set[Int], decision: Decision): Set[Int] = {
    val variable: Int = decision.`var`()
    (state - variable) diff neighbors(variable)
  }

  override def transitionCost(state: Set[Int], decision: Decision): Double =
    -weight(decision.`var`()) * decision.`val`()

  override def evaluate(solution: Array[Int]): Double = {
    require(
      solution.length == nbVars(),
      s"The solution ${solution.mkString("[", ", ", "]")} does not cover all the ${nbVars()} variable"
    )

    val independentSet: ArrayBuffer[Int] = ArrayBuffer()
    var value: Int                       = 0
    solution.zipWithIndex.foreach { case (i, v) =>
      if (v == 1) {
        independentSet += v
        value += weight(i)
      }
    }

    for (i <- independentSet.indices) {
      for (j <- i + 1 until independentSet.length) {
        val from = independentSet(i)
        val to   = independentSet(j)
        require(
          !neighbors(from).contains(to),
          s"The solution ${solution.mkString("[", ", ", "]")} is not an independent set. Nodes $from and $to are adjacent"
        )
      }
    }

    -value
  }

  override def toString: String = {
    val weightStr = weight.zipWithIndex.map { case (w, n) => s"\t$n: $w" }.mkString("\n")
    val neighStr = neighbors.zipWithIndex
      .map { case (neigh, n) =>
        s"\t$n: $neigh"
      }
      .mkString("\n")

    s"""Nodes: $nodes
       |Weight:
       |$weightStr
       |Neighbors:
       |$neighStr
       |""".stripMargin

  }
}
