package org.ddolibscala.modeling

import org.ddolib.ddo.core.Decision

import scala.jdk.CollectionConverters.IteratorHasAsJava
import scala.jdk.OptionConverters.RichOption

trait Problem[T] extends org.ddolib.modeling.Problem[T] {

  def nextValues(state: T, variable: Int): Iterable[Int]

  def optimal: Option[Double] = None

  override def nbVars(): Int

  override def initialState(): T

  override def initialValue(): Double

  override def transition(state: T, decision: Decision): T

  override def transitionCost(state: T, decision: Decision): Double

  override def evaluate(solution: Array[Int]): Double

  // METHODS TO CONVERT SCALA OBJECTS INTO JAVA OBJECTS

  final override def domain(state: T, variable: Int): java.util.Iterator[java.lang.Integer] = {
    nextValues(state, variable).map(i => java.lang.Integer.valueOf(i)).iterator.asJava
  }

  final override def optimalValue(): java.util.Optional[java.lang.Double] =
    optimal.map(d => java.lang.Double.valueOf(d)).toJava

}
