package org.ddolibscala.modeling

object DefaultFastLowerBound {

  def apply[T]() = new DefaultFastLowerBound[T]()

}

class DefaultFastLowerBound[T] extends FastLowerBound[T] {

  override def lowerBound(state: T, variables: Iterable[Int]): Double = Int.MinValue.toDouble
}
