package org.ddolibscala.modeling

import scala.jdk.CollectionConverters.CollectionHasAsScala

trait FastLowerBound[T] extends org.ddolib.modeling.FastLowerBound[T] {

  def lowerBound(state: T, variables: Iterable[Int]): Double

  final override def fastLowerBound(state: T, variables: java.util.Set[java.lang.Integer]): Double =
    lowerBound(state, variables.asScala.view.map(_.intValue))
}
