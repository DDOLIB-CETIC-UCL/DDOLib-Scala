package be.cetic.ddolibscala.examples.layered.knapsack
import be.cetic.ddolibscala
import be.cetic.ddolibscala.layered.modeling.Problem
import org.ddolib.layered.solving.ddo.core.Decision
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.Using


/** Companion object of the [[KnapSack]] class. */
object KnapSack {

  def apply(capa: Int, profit: Array[Int], weight: Array[Int]): KnapSack =
    new KnapSack(capa, profit, weight)

  /** Reads a KnapSack instance from a file.
   * Format : ...
   */
  def apply(fname: String): KnapSack = {
    val weight: ArrayBuffer[Int] = ArrayBuffer()
    val profit: ArrayBuffer[Int] = ArrayBuffer()
    var capa: Int = 0

    Using.resource(Source.fromFile(fname)) { source =>
      val lines = source.getLines()

      val header = lines.next().trim.split("\\s+")
      val nbItems = header(0).toInt
      capa = header(1).toInt

      for (_ <- 0 until nbItems) {
        val tokens = lines.next().trim.split("\\s+")
        profit += tokens(0).toInt
        weight += tokens(1).toInt
      }
    }

    val problem = KnapSack(capa, profit.toArray, weight.toArray)
    problem.name = Some(fname)
    problem
  }
}

/** Represents an instance of the Knapsack Problem as a
 * [[be.cetic.ddolibscala.layered.modeling.Problem]].
 *
 * <p> The problem consists of choosing a subset of items to maximize total profit without
 * exceeding a fixed capacity. Each item can either be taken or left out. </p>
 * <p> The state of the problem is represented by an Int indicating the remaining capacity. </p>
 *
 * @param capa
 *   the total capacity of the knapsack
 * @param profit
 *   the profit of each item
 * @param weight
 *   the weight of each item
 */
class KnapSack(val capa: Int, val profit: Array[Int], val weight: Array[Int]) extends Problem[Int] {

  private var name: Option[String] = None

  override def nbVars(): Int = weight.length // One decision variable per item

  override def initialState(): Int = capa // Root: no decisions made yet, full capacity is available

  override def initialValue(): Double = 0.0 // No profit accumulated yet, no items taken

  override def domainValues(state: Int, variable: Int): Iterable[Int] = {
    if (state >= weight(variable)) List(0, 1)
    else List(0)
  }

  override def transition(state: Int, decision: Decision): Int = {
    state - weight(decision.variable()) * decision.value()
  }

  override def transitionCost(state: Int, decision: Decision): Double = {
    -profit(decision.variable()) * decision.value()
  }

  override def evaluate(solution: Array[Int]): Double = {
    -solution.indices.map(i => profit(i) * solution(i)).sum
  } // Total cost

}
