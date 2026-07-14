package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.SearchStatistic

/** Wraps a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solver/Solution.html org.ddolib.layered.solver.Solution]].
  */
class Solution private[ddolibscala] (javaSolution: org.ddolib.layered.solver.Solution) {

  def value(): Double = javaSolution.value()

  def solution(): Array[Int] = javaSolution.solution()

  def statistics(): SearchStatistic = javaSolution.statistics()

  def searchTime(): String = javaSolution.searchTime()

  override def toString: String = javaSolution.toString
}
