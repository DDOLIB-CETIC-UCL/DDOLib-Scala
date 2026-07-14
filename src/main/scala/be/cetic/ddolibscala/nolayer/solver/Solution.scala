package be.cetic.ddolibscala.nolayer.solver

import be.cetic.ddolibscala.SearchStatistic

import scala.jdk.CollectionConverters.ListHasAsScala

/** Wraps a
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solver/Solution.html org.ddolib.nolayer.solver.Solution]].
  */
class Solution private[ddolibscala] (javaSolution: org.ddolib.nolayer.solver.Solution) {

  def value(): Double = javaSolution.value()

  def solution(): Seq[Int] = javaSolution.solution().asScala.toSeq.map(_.toInt)

  def statistics(): SearchStatistic = javaSolution.statistics()

  def searchTime(): String = javaSolution.searchTime()

  override def toString: String = javaSolution.toString
}
