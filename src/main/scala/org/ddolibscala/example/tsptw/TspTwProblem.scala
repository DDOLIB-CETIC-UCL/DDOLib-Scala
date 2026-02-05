package org.ddolibscala.example.tsptw

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Problem

import scala.collection.immutable.BitSet
import scala.io.Source
import scala.util.Using

/** Companion object of the [[TspTwProblem]] class.
  */
object TspTwProblem {

  /** Returns a representation of the TSPTW given a time matrix
    *
    * @param timeMatrix
    *   matrix such that `timeMatrix(i)(j)` is the travel time between nodes `i` and `j`
    * @param timeWindows
    *   for each node, contains the associated time window
    * @param optimal
    *   if known, the optimal solution of this problem. Useful for tests
    * @param name
    *   an optional name to override the default `toString`
    * @return
    *   a representation of the TSPTW
    */
  def apply(
    timeMatrix: Array[Array[Int]],
    timeWindows: Array[TimeWindow],
    optimal: Option[Double] = None,
    name: Option[String] = None
  ): TspTwProblem = {
    new TspTwProblem(timeMatrix.length, i => j => timeMatrix(i)(j), timeWindows, optimal, name)
  }

  /** Returns a representation of the TSPTW given the path to a data file.
    *
    * ===Data file format===
    *   - First line: number of nodes (variables). Optionally, a second value may be provided for
    *     the optimal objective.
    *   - Next lines: the distance matrix, one row per node.
    *   - Following lines: time windows for each node, specified as two integers (start and end) per
    *     line.
    *
    * @param fname
    *   the path to the data file
    * @return
    *   a representation of the TSPTW
    */
  def apply(fname: String): TspTwProblem = {
    Using.resource(Source.fromFile(fname)) { reader =>
      val lines = reader.getLines().map(_.trim).filter(l => l.nonEmpty && !l.startsWith("#")).toList

      val firstLine: Array[String]     = lines.head.split("\\s+")
      val numVar: Int                  = firstLine(0).toInt
      val optimalValue: Option[Double] =
        if (firstLine.length == 2) Some(firstLine(1).toDouble) else None

      val timeMatrix: Array[Array[Int]] =
        lines.slice(1, numVar + 1).map(line => line.split("\\s+").map(_.toInt)).toArray

      val tw: Array[TimeWindow] = lines
        .slice(numVar + 1, 2 * numVar + 1)
        .map { line =>
          val bounds = line.split("\\s+")
          TimeWindow(bounds(0).toInt, bounds(1).toInt)
        }
        .toArray

      TspTwProblem(timeMatrix, tw, optimalValue, Some(fname))
    }

  }
}

/** Class representing an instance of the Traveling Salesperson Problem with Time Windows (TSPTW).
  *
  * <p> Each node has a time window during which it must be visited, and travel between nodes is
  * defined by a distance matrix. This class implements the [[org.ddolibscala.modeling.Problem]]
  * trait for use in decision diagram solvers. </p>
  *
  * <p> The problem instance can optionally provide the known optimal solution. </p>
  *
  * @param numNodes
  *   the number of nodes to route (including the start point)
  * @param time
  *   function such that `time(i)(j)` returns the travel time between node `i` and node `j`
  * @param timeWindows
  *   for each node, contains the associated time window
  * @param _optimal
  *   if known, the optimal solution of this problem. Useful for tests
  * @param name
  *   an optional name to override the default `toString`
  */
class TspTwProblem(
  numNodes: Int,
  val time: Int => Int => Int,
  val timeWindows: Array[TimeWindow],
  _optimal: Option[Double] = None,
  name: Option[String] = None
) extends Problem[TspTwState] {

  override def nbVars(): Int = numNodes

  override def initialState(): TspTwState = {
    val pos: Position = TspNode(0)
    val time: Int     = 0
    val must: BitSet  = BitSet.fromSpecific(1 until numNodes)
    val maybe: BitSet = BitSet.empty
    TspTwState(pos, time, must, maybe, 0)
  }

  override def initialValue(): Double = 0

  override def domainValues(state: TspTwState, variable: Int): Iterable[Int] = {
    if (state.depth == nbVars() - 1 && reachable(state, 0)) BitSet(0)
    else if (state.mustVisit.forall(node => reachable(state, node))) {
      var toReturn: BitSet = BitSet.fromSpecific(state.mustVisit)

      if (state.mustVisit.size < nbVars() - state.depth)
        toReturn = toReturn union state.maybeVisit.filter(node => reachable(state, node))

      toReturn
    } else Nil

  }

  override def transition(state: TspTwState, decision: Decision): TspTwState = {
    val target: Int      = decision.`val`()
    val newPos: TspNode  = TspNode(target)
    val newTime: Int     = arrivalTime(state, target)
    val newMust: BitSet  = state.mustVisit - target
    val newMaybe: BitSet = state.maybeVisit - target
    val newDepth: Int    = state.depth + 1
    TspTwState(newPos, newTime, newMust, newMaybe, newDepth)
  }

  override def transitionCost(state: TspTwState, decision: Decision): Double = {
    val to: Int = decision.`val`()

    val travel: Int  = minDuration(state, to)
    val arrival: Int = state.time + travel;
    val waiting: Int = (timeWindows(to).start - arrival) max 0

    travel + waiting
  }

  override def evaluate(solution: Array[Int]): Double = {
    val solutionStr: String = "0 -> " + solution.mkString(" -> ")
    require(
      solution.length == nbVars(),
      s"The solution $solutionStr does not cover the ${nbVars()} variables"
    )

    require(
      solution.length == solution.distinct.length,
      s"Solution $solutionStr has duplicated nodes and does not reach each node"
    )

    var currentTime: Int = time(0)(solution(0)) max timeWindows(solution(0)).start
    for (i <- 1 until nbVars()) {
      val from: Int        = solution(i - 1)
      val to: Int          = solution(i)
      val arrivalTime: Int = currentTime + time(from)(to)
      require(
        arrivalTime <= timeWindows(to).end,
        s""" This solution does respect time windows
           | You arrive at node $to at time $arrivalTime. Its time window is ${timeWindows(to)}
           |""".stripMargin
      )
      currentTime = arrivalTime max timeWindows(to).start
    }

    currentTime

  }

  override def optimal: Option[Double] = _optimal

  /** Returns the travel time between this node */
  private[tsptw] def minDuration(from: TspTwState, to: Int): Int = {
    from.position match {
      case TspNode(pos)        => time(pos)(to)
      case VirtualNodes(nodes) => nodes.map(time(_)(to)).min
    }
  }

  /** Returns whether the node `to` is reachable from node `from` according its time window. */
  private[tsptw] def reachable(from: TspTwState, to: Int): Boolean = {
    val duration = minDuration(from, to)
    from.time + duration <= timeWindows(to).end
  }

  /** Returns the maximum between the arrival time at node `to` from node `from` and the start
    * of `to`'s time window.
    */
  private def arrivalTime(from: TspTwState, to: Int): Int = {
    val arrival: Int = from.time + minDuration(from, to)
    arrival max timeWindows(to).start
  }

  override def toString: String = {
    val timeMatrixStr = Array
      .tabulate(nbVars(), nbVars())((i, j) => time(i)(j))
      .map(_.map(x => f"$x%3s").mkString(" "))
      .mkString("\n")
    val str =
      s"""Num nodes: $numNodes
         |Times windows: ${timeWindows.mkString(", ")}
         |Time matrix:
         |$timeMatrixStr
         |""".stripMargin

    name.getOrElse(str)
  }
}
