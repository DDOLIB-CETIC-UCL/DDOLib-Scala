package org.ddolibscala.example.misp

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Problem

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

/** Companion object of the   [[MispProblem]] class. */
object MispProblem {

  /** Returns an instance of the Maximum Independent Set Problem (MISP) as a
    * [[org.ddolibscala.modeling.Problem]].
    *
    * @param nodes
    *   all the nodes of the graph
    * @param neighbors
    *   adjacency list for each node
    * @param weights
    *   weight of each node
    * @param optimal
    *   the value of the optimal solution if known
    * @return
    *   an instance of the Maximum Independent Set Problem (MISP) as a
    *   [[org.ddolibscala.modeling.Problem]].
    */
  def apply(
    nodes: Set[Int],
    neighbors: Array[Set[Int]],
    weights: Array[Int],
    optimal: Option[Double] = None
  ): MispProblem = new MispProblem(nodes, neighbors, weights, optimal)

  /** Returns an instance of the Maximum Independent Set Problem (MISP) as a
    * [[org.ddolibscala.modeling.Problem]] by reading a file.
    *
    * @param fname
    *   The path to the data file
    * @return
    *   an instance of the Maximum Independent Set Problem (MISP) as a
    *   [[org.ddolibscala.modeling.Problem]].
    */
  def apply(fname: String): MispProblem = {
    val weights: ArrayBuffer[Int]  = ArrayBuffer()
    var neighbors: Array[Set[Int]] = Array()
    var opti: Option[Double]       = None
    var numNodes: Int              = 0

    Using(Source.fromFile(fname)) { source =>
      val (nodesLines, edgesLines) = source.getLines().drop(1).span(!_.contains("--"))

      for (line <- nodesLines if line.trim.nonEmpty) {
        if (line.contains("optimal")) {
          val optiStr: String       = line.replace(";", "")
          val tokens: Array[String] = optiStr.split("=")
          opti = Some(tokens(1).toDouble)
        } else if (line.contains("weight")) {
          var w: String = line.trim.split(" ")(1)
          w = w.replace("[weight=", "").replace("];", "")
          weights += w.toInt
        } else {
          weights += 1
        }
      }

      numNodes = weights.length
      neighbors = Array.fill(numNodes)(Set.empty)

      for (line <- edgesLines.takeWhile(_ != "}") if line.trim.nonEmpty) {
        val tokens: Array[String] = line.trim.replace(" ", "").replace(";", "").split("--")
        val source: Int           = tokens(0).toInt - 1
        val target: Int           = tokens(1).toInt - 1
        neighbors(source) = neighbors(source) + target
        neighbors(target) = neighbors(target) + source
      }
    }

    MispProblem((0 until numNodes).toSet, neighbors, weights.toArray, opti)

  }
}

/** Represents an instance of the Maximum Independent Set Problem (MISP) as a
  * [[org.ddolibscala.modeling.Problem]].
  *
  * <p> The problem is defined on a weighted undirected graph. Each node can either be included in
  * the independent set or not, and selected nodes cannot be adjacent. </p> <p> The state of the
  * problem is represented by a Set indicating which nodes can still be selected. The solver
  * explores decisions for each node to build an independent set of maximum weight. </p>
  *
  * @param nodes
  *   all the nodes of the graph
  * @param neighbors
  *   adjacency list for each node
  * @param weights
  *   weight of each node
  * @param _optimal
  *   the value of the optimal solution if known
  */
class MispProblem(
  nodes: Set[Int],
  val neighbors: Array[Set[Int]],
  val weights: Array[Int],
  _optimal: Option[Double]
) extends Problem[Set[Int]] {

  override def optimal: Option[Double] = _optimal

  override def nbVars(): Int = weights.length

  override def initialState(): Set[Int] = nodes

  override def initialValue(): Double = 0.0

  override def domainValues(state: Set[Int], variable: Int): Iterable[Int] = {
    if (state.contains(variable)) List(0, 1)
    else List(0)
  }

  override def transition(state: Set[Int], decision: Decision): Set[Int] = {
    val variable: Int = decision.`var`()
    if (decision.`val`() == 1) (state - variable) diff neighbors(variable)
    else state - variable
  }

  override def transitionCost(state: Set[Int], decision: Decision): Double =
    -weights(decision.`var`()) * decision.`val`()

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
        value += weights(i)
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
    val weightsStr = weights.zipWithIndex.map { case (w, n) => s"\t$n: $w" }.mkString("\n")
    val neighStr = neighbors.zipWithIndex.map { case (neigh, n) => s"\t$n: $neigh" }.mkString("\n")

    s"""Nodes: $nodes
       |Weight:
       |$weightsStr
       |Neighbors:
       |$neighStr
       |""".stripMargin

  }
}
