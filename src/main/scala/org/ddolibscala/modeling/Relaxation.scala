package org.ddolibscala.modeling

import org.ddolib.ddo.core.Decision

import scala.jdk.CollectionConverters.IteratorHasAsScala

/** Defines the relaxation that may be applied to the given problem. In particular, the [[merge]]
  * method from this trait defines how the nodes of a layer may be combined to provide an upper
  * bound approximation standing for an arbitrarily selected set of nodes.
  *
  * @tparam T
  *   the type of states
  */
trait Relaxation[T] extends org.ddolib.modeling.Relaxation[T] {

  /** Merges the given states to create a NEW state which is an over approximation of all the
    * covered states.
    *
    * @param statesToMerge
    *   the set of states that must be merged
    * @return
    *   a new state which is an over approximation of all the considered `states`.
    */
  def merge(statesToMerge: Iterable[T]): T

  /** Relaxes the edge that used to go from the `from` state to the `to` state and computes the cost
    * of the new edge going from the `from` state to the `merged` state. The decision which is being
    * relaxed is given by `decision` and the value of the not relaxed arc is `cost`.
    *
    * @param from
    *   the origin of the relaxed arc
    * @param to
    *   the destination of the relaxed arc (before relaxation)
    * @param merged
    *   the destination of the relaxed arc (after relaxation)
    * @param decision
    *   the decision which is being challenged
    * @param cost
    *   the cost of the not relaxed arc which used to go from the `from` state to the `to` state
    * @return
    *   the cost of the relaxed edge
    */
  override def relaxEdge(from: T, to: T, merged: T, decision: Decision, cost: Double): Double

  // METHOD THAT CONVERT SCALA OBJECTS TO JAVA OBJECTS

  /** Used by the solver. Converts the input and output of [[merge]] from Java to Scala and vice
    * versa.
    */
  final override def mergeStates(states: java.util.Iterator[T]): T =
    merge(states.asScala.to(Iterable))

}
