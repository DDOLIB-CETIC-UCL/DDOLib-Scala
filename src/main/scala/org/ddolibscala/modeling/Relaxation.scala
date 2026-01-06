package org.ddolibscala.modeling

import org.ddolib.ddo.core.Decision

import scala.jdk.CollectionConverters.IteratorHasAsScala

trait Relaxation[T] extends org.ddolib.modeling.Relaxation[T] {

  def merge(statesToMerge: Iterable[T]): T

  override def relaxEdge(from: T, to: T, merged: T, decision: Decision, cost: Double): Double

  final override def mergeStates(states: java.util.Iterator[T]): T =
    merge(states.asScala.to(Iterable))

}
