package org.ddolibscala.modeling

import org.ddolib.ddo.core.Decision

import scala.jdk.CollectionConverters.IteratorHasAsJava
import scala.jdk.OptionConverters.RichOption

/** `Problem` defines the state space, the transitions between states induced by decisions, and the
  * objective values associated with those transitions. Implementations provide the essential
  * operations required by solvers.
  *
  * @tparam T
  *   the type representing a state in the problem
  */
trait Problem[T] extends org.ddolib.modeling.Problem[T] {

  /** Returns the domain of possible values for a given variable when applied to a specific state.
    *
    * @param state
    *   the current state
    * @param variable
    *   the variable index whose domain is queried
    * @return
    *   ll feasible values for the variable in this state
    */
  def domainValues(state: T, variable: Int): Iterable[Int]

  /** Returns the optimal value of the problem if known
    *
    * @return
    *   the optimal value of the problem if known
    */
  def optimal: Option[Double] = None

  /** The total number of decision variables in this problem.
    * @return
    *   the total number of decision variables in this problem
    */
  override def nbVars(): Int

  /** Returns the initial state of the problem.
    *
    * @return
    *   the state representing the starting point of the optimization
    */
  override def initialState(): T

  /** Returns the initial objective value associated with the initial state.
    *
    * @return
    *   the starting value of the objective function
    */
  override def initialValue(): Double

  /** Applies a decision to a state, computing the next state according to the problem's transition
    * function.
    *
    * @param state
    *   the state from which the transition originates
    * @param decision
    *   the decision to apply
    * @return
    *   the resulting state after applying the decision
    */
  override def transition(state: T, decision: Decision): T

  /** Computes the change in objective value resulting from applying a decision to a given state.
    *
    * @param state
    *   the state from which the transition originates
    * @param decision
    *   the decision to apply
    * @return
    *   the incremental objective cost/value associated with this decision
    */
  override def transitionCost(state: T, decision: Decision): Double

  /** Given a solution such that `solution(i)` is the value of the variable`x_i`, returns the value
    * of this solution and checks if the solution respects the problem's constraints.
    * @note
    *   For maximization problems, the returned value is minus the computed value.
    *
    * @param solution
    *   A solution of the problem.
    * @return
    *   The value of the input solution.
    */
  override def evaluate(solution: Array[Int]): Double

  // METHODS TO CONVERT SCALA OBJECTS INTO JAVA OBJECTS

  /** Used by the solver. Converts the input of ouput of [[domainValues]] from Java to Scala and
    * vice versa.
    */
  final override def domain(state: T, variable: Int): java.util.Iterator[java.lang.Integer] = {
    domainValues(state, variable).map(i => java.lang.Integer.valueOf(i)).iterator.asJava
  }

  /** Used by the solver. Converts the input of ouput of [[optimal]] from Java to Scala and vice
    * versa.
    */
  final override def optimalValue(): java.util.Optional[java.lang.Double] =
    optimal.map(d => java.lang.Double.valueOf(d)).toJava

}
