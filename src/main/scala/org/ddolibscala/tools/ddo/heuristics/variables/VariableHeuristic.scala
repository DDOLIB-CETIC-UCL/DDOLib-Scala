package org.ddolibscala.tools.ddo.heuristics.variables

import scala.jdk.CollectionConverters.{IterableHasAsScala, IteratorHasAsScala}

trait VariableHeuristic[T] extends org.ddolib.ddo.core.heuristics.variable.VariableHeuristic[T] {

  def next(variables: Iterable[Int], states: Iterable[T]): Int

  final override def nextVariable(
    variables: java.util.Set[java.lang.Integer],
    states: java.util.Iterator[T]
  ): java.lang.Integer = {
    val scalaVar: Iterable[Int]  = variables.asScala.view.map(_.intValue())
    val scalaStates: Iterable[T] = states.asScala.to(Iterable)
    java.lang.Integer.valueOf(next(scalaVar, scalaStates))
  }
}
