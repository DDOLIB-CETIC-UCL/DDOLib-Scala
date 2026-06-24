package be.cetic.ddolibscala.modeling.nolayer

import scala.jdk.CollectionConverters.IteratorHasAsJava
import scala.jdk.OptionConverters.RichOption

/** `Problem` defines the state space, the transitions between states induced by labels, and the
 * objective values associated with those transitions. Implementations provide the essential
 * operations required by solvers, without an a priori fixed number of variables.
 *
 * @tparam T
 * the type representing a state in the problem
 */
trait Problem[T] extends org.ddolib.modeling.nolayer.Problem[T] {

  /** Returns the domain of possible labels/actions from a given state.
   *
   * @param state
   * the current state
   * @return
   * all feasible labels for the given state
   */
  def domainLabels(state: T): Iterable[Int]

  /** Returns the known optimal value of the problem, if available.
   *
   * @return
   * an [[Option]] containing the known optimal value, or None if unknown
   */
  def optimal: Option[Double] = None

  /** Returns the initial state of the problem.
   *
   * @return
   * the state representing the starting point of the optimization
   */
  override def initialState(): T

  /** Returns the initial objective value associated with the initial state.
   *
   * @return
   * the starting value of the objective function
   */
  override def initialValue(): Double

  /** Checks whether the given state is a target (sink) state.
   *
   * @param state
   * the current state
   * @return
   * true if the state is a target state, false otherwise
   */
  override def isTarget(state: T): Boolean

  /** Applies a label/action to a state, computing the next state according to the problem's
   * transition function.
   *
   * @param state
   * the state from which the transition originates
   * @param label
   * the label to apply
   * @return
   * the resulting state after applying the label
   */
  override def transition(state: T, label: Int): T

  /** Computes the change in objective value resulting from applying a label to a given state.
   *
   * @param state
   * the state from which the transition originates
   * @param label
   * the label to apply
   * @return
   * the incremental objective cost/value associated with this label
   */
  override def transitionCost(state: T, label: Int): Double

  /** Given a solution (a sequence of applied labels), returns its value and checks
   * if the solution respects the problem's constraints.
   *
   * @param solution
   * A solution of the problem (sequence of labels).
   * @return
   * The value of the input solution
   */
  override def evaluate(solution: Array[Int]): Double

  // METHODS TO CONVERT SCALA OBJECTS INTO JAVA OBJECTS

  /** Used by the solver. Converts the output of [[domainLabels]] from Scala to Java.
   */
  final override def domain(state: T): java.util.Iterator[java.lang.Integer] = {
    domainLabels(state).map(i => java.lang.Integer.valueOf(i)).iterator.asJava
  }

  /** Used by the solver. Converts the output of [[optimal]] from Scala to Java.
   */
  final override def optimalValue(): java.util.Optional[java.lang.Double] =
    optimal.map(d => java.lang.Double.valueOf(d)).toJava

}