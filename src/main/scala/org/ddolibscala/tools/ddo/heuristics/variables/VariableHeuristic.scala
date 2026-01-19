package org.ddolibscala.tools.ddo.heuristics.variables

import scala.jdk.CollectionConverters.{IterableHasAsScala, IteratorHasAsScala}

/** Defines a strategy for selecting the next decision variable to branch on during the construction
  * or exploration of a decision diagram.
  *
  * <p> A `VariableHeuristic` is responsible for determining, at each expansion step, which variable
  * should be fixed next among the remaining unassigned ones. It can use information from the
  * current layer’s states to guide this choice. </p>
  *
  * <p>Heuristics of this kind are essential in dynamic programming, search trees, and decision
  * diagrams, as they influence the structure of the diagram and the efficiency of the exploration
  * process. A well-chosen variable ordering can drastically reduce the diagram width and
  * computation time.</p>
  *
  * @tparam T
  *   the type representing the problem state
  */
trait VariableHeuristic[T] extends org.ddolib.ddo.core.heuristics.variable.VariableHeuristic[T] {

  /** Selects the next variable to branch on given the current set of unassigned variables and the
    * states of the next layer. <p> The heuristic can analyze the provided states to estimate which
    * variable will best separate or discriminate the search space, improving pruning or
    * convergence. If no meaningful decision can be made, `null` may be returned to indicate that
    * the choice should be deferred or decided by a default mechanism. </p>
    *
    * @param variables
    *   the set of variable indices that are still unassigned
    * @param states
    *   an iterator over the current states in the next layer
    * @return
    *   the index of the next variable to branch on, or `null` if no decision can be made at this
    *   point
    */
  def next(variables: Iterable[Int], states: Iterable[T]): Int

  /** Used by the solver. Converts the input and output of [[next]] from Java to Scala and vice
    * versa.
    */
  final override def nextVariable(
    variables: java.util.Set[java.lang.Integer],
    states: java.util.Iterator[T]
  ): java.lang.Integer = {
    val scalaVar: Iterable[Int]  = variables.asScala.view.map(_.intValue())
    val scalaStates: Iterable[T] = states.asScala.to(Iterable)
    java.lang.Integer.valueOf(next(scalaVar, scalaStates))
  }
}
