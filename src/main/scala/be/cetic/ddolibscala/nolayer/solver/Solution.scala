package be.cetic.ddolibscala.nolayer.solver

import be.cetic.ddolibscala.SearchStatistic
import org.ddolib.common.solver.stat.DdoStats

import scala.jdk.CollectionConverters.{ListHasAsScala, SeqHasAsJava}

/** Wraps a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solver/Solution.html org.ddolib.nolayer.solver.Solution]].
  */
class Solution private[ddolibscala] (
  private[ddolibscala] val javaSolution: org.ddolib.nolayer.solver.Solution
) {

  def value(): Double = javaSolution.value()

  def solution(): Seq[Int] = javaSolution.solution().asScala.toSeq.map(_.toInt)

  /** Returns whether this solution is empty, i.e. no solution was found. */
  def isEmpty: Boolean = javaSolution.isEmpty

  def statistics(): SearchStatistic = javaSolution.statistics()

  def searchTime(): String = javaSolution.searchTime()

  override def toString: String = javaSolution.toString
}

object Solution {

  /** Builds a solution from a known sequence of labels, typically to give it as initial solution
    * to a solver.
    *
    * The sequence must lead from the initial state to a target state. Its value is trusted: it is
    * only checked against the problem's `evaluate` method when the solver runs with a debug mode
    * other than [[be.cetic.ddolibscala.common.util.DebugMode.Off]].
    *
    * @param labels
    *   the sequence of labels applied from the initial state
    * @param value
    *   the objective value of the solution
    * @return
    *   a solution wrapping the given labels and value
    */
  def apply(labels: Seq[Int], value: Double): Solution = {
    val javaLabels = labels.map(Integer.valueOf).asJava
    // Java reads the value of a solution from the incumbent of its statistics.
    val stats = new DdoStats(0, value)
    new Solution(new org.ddolib.nolayer.solver.Solution(javaLabels, stats))
  }
}
