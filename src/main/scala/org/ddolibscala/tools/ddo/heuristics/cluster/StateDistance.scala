package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolib.ddo.core.mdd.NodeSubProblem

/** Trait defining a distance function between states, used to form clusters when deciding which
  * nodes on a layer of a decision diagram should be merged.
  *
  * <p> The distance function must satisfy the following properties:</p>
  *   - Non-negative: distance(a, b) ≥ 0
  *   - Symmetric: distance(a, b) = distance(b, a)
  *   - Triangle inequality: distance(a, c) ≤ distance(a, b) + distance(b, c)
  *
  * @tparam T
  *   the type of states
  */
trait StateDistance[T] extends org.ddolib.ddo.core.heuristics.cluster.StateDistance[T] {

  /** Computes the discrete distance between two states.
    *
    * @param a
    *   the first state
    * @param b
    *   the second state
    * @return
    *   the distance between `a` and `b`
    */
  override def distance(a: T, b: T): Double

  /** Computes the distance between two nodes of a subproblem.
    *
    * <p> By default, returns `0`. Can be overridden for more precise node-level distances.</p>
    *
    * @param a
    *   the first node
    * @param b
    *   the second node
    * @return
    *   the distance between the nodes
    */
  override def distance(a: NodeSubProblem[T], b: NodeSubProblem[T]): Double = super.distance(a, b)

  /** Computes the distance between a state and the root of the search/tree.
    *
    * <p> By default, returns 0. Can be overridden for root-distance computations.</p>
    *
    * @param state
    *   the state to measure
    * @return
    *   the distance to the root
    */
  override def distanceWithRoot(state: T): Double = super.distanceWithRoot(state)
}
