package org.ddolibscala.tools.ddo.frontier

import org.ddolib.ddo.core.SubProblem
import org.ddolib.ddo.core.frontier.CutSetType

trait Frontier[T] extends org.ddolib.ddo.core.frontier.Frontier[T] {

  /** Adds a new subproblem to the frontier for future exploration.
    *
    * <p> This method inserts a node into the internal priority structure of the frontier (e.g., a
    * priority queue ordered by the subproblem’s upper bound or heuristic value). </p>
    *
    * @param subProblem
    *   the subproblem to be added to the frontier
    */
  override def push(subProblem: SubProblem[T]): Unit

  /** Extracts and returns the most promising subproblem from the frontier.
    *
    * <p> The solver assumes that subproblems are popped in <b>descending order of upper bound</b>,
    * i.e., the node with the best potential objective value should be returned first. </p>
    *
    * <p>If the frontier is empty, this method may return `null`.</p>
    *
    * @return
    *   the subproblem with the highest priority in the frontier, or `null` if empty
    */
  override def pop(): SubProblem[T]

  /** Removes all subproblems currently stored in the frontier.
    *
    * <p> This operation resets the internal structure and is typically used when restarting a
    * search or reinitializing the solver. </p>
    */
  override def clear(): Unit

  /** Returns the number of subproblems currently stored in the frontier.
    *
    * @return
    *   the number of nodes in the frontier
    */
  override def size(): Int

  /** Returns the type of cut set associated with this frontier.
    *
    * <p> The cut set type determines the strategy used to define which nodes belong to the frontier
    * (e.g.,
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/frontier/CutSetType.html#LastExactLayer CutSetType.LastExactLayer]]
    * or
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/frontier/CutSetType.html#Frontier CutSetType.Frontier]]).
    * This affects how the solver manages layers during compilation. </p>
    *
    * @return
    *   the
    *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/frontier/CutSetType.html CutSetType]]
    *   representing the frontier strategy
    */
  override def cutSetType(): CutSetType

  /** Returns the current '''best upper bound''' among all subproblems stored in the frontier.
    *
    * <p> The "best" bound depends on the problem type (e.g., maximum upper bound for minimization
    * problems). This method is generally used by the solver to monitor convergence or update global
    * bounds. </p>
    *
    * @note
    *   Implementations should throw an exception if this method is called when the frontier is
    *   empty.
    *
    * @return
    *   the best upper bound value among subproblems in the frontier
    */
  override def bestInFrontier(): Double

  /** Checks whether the frontier is empty.
    *
    * @return
    *   whether the frontier is empty
    */
  override def isEmpty: Boolean = size() == 0
}
