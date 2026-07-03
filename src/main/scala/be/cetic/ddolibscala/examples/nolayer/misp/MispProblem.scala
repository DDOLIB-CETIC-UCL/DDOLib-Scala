package be.cetic.ddolibscala.examples.nolayer.misp

import be.cetic.ddolibscala.nolayer.modeling.Problem

import scala.collection.immutable.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using

/** Companion object of the   [[MispProblem]] class. */
object MispProblem {

  /** Returns an instance of the Maximum Independent Set Problem (MISP) as a [[Problem]].
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
    *   an instance of the Maximum Independent Set Problem (MISP) as a [[Problem]].
    */
  def apply(
    nodes: BitSet,
    neighbors: Array[BitSet],
    weights: Array[Int],
    optimal: Option[Double] = None
  ): MispProblem = new MispProblem(nodes, neighbors, weights, optimal)

  /** Returns an instance of the Maximum Independent Set Problem (MISP) as a [[Problem]] by reading
    * a file.
    *
    * @param fname
    *   The path to the data file
    * @return
    *   an instance of the Maximum Independent Set Problem (MISP) as a [[Problem]].
    */
  def apply(fname: String): MispProblem = {
    val weights: ArrayBuffer[Int] = ArrayBuffer()
    var neighbors: Array[BitSet]  = Array()
    var opti: Option[Double]      = None
    var numNodes: Int             = 0

    Using.resource(Source.fromFile(fname)) { source =>
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
      neighbors = Array.fill(numNodes)(BitSet())

      for (line <- edgesLines.takeWhile(_ != "}") if line.trim.nonEmpty) {
        val tokens: Array[String] = line.trim.replace(" ", "").replace(";", "").split("--")
        val source: Int           = tokens(0).toInt - 1
        val target: Int           = tokens(1).toInt - 1
        neighbors(source) = neighbors(source) + target
        neighbors(target) = neighbors(target) + source
      }
    }

    val problem =
      MispProblem(BitSet.fromSpecific(0 until numNodes), neighbors, weights.toArray, opti)
    problem.name = Some(fname)
    problem
  }
}

class MispProblem(
  nodes: BitSet,
  val neighbors: Array[BitSet],
  val weights: Array[Int],
  _optimal: Option[Double]
) extends Problem[BitSet] {

  private var name: Option[String] = None

  def numNodes: Int = nodes.size

  override def domainLabels(state: BitSet): Iterable[Int] = state

  override def initialState(): BitSet = nodes

  override def initialValue(): Double = 0

  override def isTarget(state: BitSet): Boolean = state.isEmpty

  override def transition(state: BitSet, label: Int): BitSet = (state - label) diff neighbors(label)

  override def transitionCost(state: BitSet, label: Int): Double = -weights(label)

  override def evaluate(solution: Array[Int]): Double = {

    for (i <- solution.indices) {
      for (j <- i + 1 until solution.length) {
        val from = solution(i)
        val to   = solution(j)
        require(
          !neighbors(from).contains(to),
          s"The solution ${solution.mkString("[", ", ", "]")} is not an independent set. Nodes $from and $to are adjacent"
        )
      }
    }

    solution.foldLeft(0)((acc, node) => acc - weights(node))
  }

  override def toString: String = {
    val weightsStr = weights.zipWithIndex.map { (w, n) => s"\t$n: $w" }.mkString("\n")
    val neighStr   = neighbors.zipWithIndex.map { (neigh, n) => s"\t$n: $neigh" }.mkString("\n")

    val problemStr =
      s"""Nodes: $nodes
         |Weight:
         |$weightsStr
         |Neighbors:
         |$neighStr
         |""".stripMargin

    name.getOrElse(problemStr)

  }

  override def optimal: Option[Double] = _optimal.map(-_)
}
