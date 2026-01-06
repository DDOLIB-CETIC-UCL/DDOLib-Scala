package org.ddolibscala.modeling

trait Dominance[T] extends org.ddolib.modeling.Dominance[T] {

  def key(state: T): Any

  override def isDominatedOrEqual(state1: T, state2: T): Boolean

  final override def getKey(state: T): AnyRef = {
    key(state).asInstanceOf[AnyRef]
  }
}
