package org.ddolibscala.example.misp

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Problem

import scala.collection.mutable.ArrayBuffer

object MispProblem {

  def apply(
    nodes: Set[Int],
    neighbors: Array[Set[Int]],
    weight: Array[Int],
    optimal: Option[Double] = None
  ): MispProblem = new MispProblem(nodes, neighbors, weight, optimal)
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
  
}
