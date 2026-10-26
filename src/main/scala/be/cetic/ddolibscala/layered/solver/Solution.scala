package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.SearchStatistic
import be.cetic.ddolibscala.layered.Decision
import org.ddolib.common.solver.stat.DdoStats

import java.util.Optional
import scala.jdk.CollectionConverters.SetHasAsJava

/** Wraps a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solver/Solution.html org.ddolib.layered.solver.Solution]].
  */
class Solution private[ddolibscala] (
  private[ddolibscala] val javaSolution: org.ddolib.layered.solver.Solution
) {

  def value(): Double = javaSolution.value()

  def solution(): Array[Int] = javaSolution.solution()

  /** Returns whether this solution is empty, i.e. no solution was found. */
  def isEmpty: Boolean = javaSolution.isEmpty

  def statistics(): SearchStatistic = javaSolution.statistics()

  def searchTime(): String = javaSolution.searchTime()

  override def toString: String = javaSolution.toString
}

object Solution {

  /** Builds a solution from a known assignment, typically to give it as initial solution to a
    * solver.
    *
    * The assignment must assign all the variables and be feasible. Its value is trusted: it is only
    * checked against the problem's `evaluate` method when the solver runs with a debug mode other
    * than [[be.cetic.ddolibscala.common.util.DebugMode.Off]].
    *
    * @param assignment
    *   an array `t` such that `t(i)` is the value assigned to the variable `i`
    * @param value
    *   the objective value of the assignment
    * @return
    *   a solution wrapping the given assignment and value
    */
  def apply(assignment: Array[Int], value: Double): Solution = {
    val decisions = assignment.indices.map(i => Decision(i, assignment(i))).toSet
    // Java reads the value of a solution from the incumbent of its statistics.
    val stats = new DdoStats(0, value)
    new Solution(new org.ddolib.layered.solver.Solution(Optional.of(decisions.asJava), stats))
  }
}
