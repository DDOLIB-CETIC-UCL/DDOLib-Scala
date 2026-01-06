package org.ddolibscala.modeling

import java.util.Optional
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

trait Problem[T] extends org.ddolib.modeling.Problem[T] {

  def nextValues(state: T, variable: Int): Iterable[Int]

  def optimal: Option[Double] = None

  // METHODS TO CONVERT SCALA OBJECTS INTO JAVA OBJECTS

  final override def domain(state: T, variable: Int): java.util.Iterator[Integer] = {
    nextValues(state, variable).map(i => java.lang.Integer.valueOf(i)).iterator.asJava
  }

  final override def optimalValue(): Optional[java.lang.Double] =
    optimal.map(d => java.lang.Double.valueOf(d)).toJava

}
