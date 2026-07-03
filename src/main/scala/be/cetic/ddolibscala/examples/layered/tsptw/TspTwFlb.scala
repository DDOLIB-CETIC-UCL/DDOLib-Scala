package be.cetic.ddolibscala.examples.layered.tsptw

import be.cetic.ddolibscala.examples.layered.tsptw.{TspNode, VirtualNodes}
import be.cetic.ddolibscala.layered.modeling.FastLowerBound

import scala.util.boundary
import scala.util.boundary.break

/** Implementation of a fast lower bound for the Traveling Salesperson Problem with Time Windows
  * (TSPTW).
  *
  * <p> This class provides a heuristic lower bound on the total tour cost starting from a given
  * [[TspTwState]]. The lower bound is computed by summing the shortest available edges from the
  * current position, including all mandatory nodes that must be visited and a selection of optional
  * nodes if needed to complete the tour. The bound also considers returning to the depot and
  * respects time window constraints. </p>
  *
  * <p> If any mandatory node is unreachable from the current state, or if completing the tour is
  * impossible within the time windows, the bound returns [[scala.Int.MaxValue]] to indicate
  * infeasibility. </p>
  *
  * <p> Precomputes the cheapest outgoing edge for each node to speed up repeated lower bound
  * calculations. </p>
  *
  * @param problem
  *   the associated TSPTW problem instance
  */
class TspTwFlb(problem: TspTwProblem) extends FastLowerBound[TspTwState] {

  private val Infinity: Int             = Int.MaxValue
  private val numVar: Int               = problem.nbVars()
  private val cheapestEdges: Array[Int] =
    Array.tabulate(numVar)(i =>
      (0 until numVar).iterator.filter(_ != i).map(problem.timeMatrix(i)).min
    )

  override def lowerBound(state: TspTwState, variables: Iterable[Int]): Double = boundary {

    // Determine the initial travel cost based on the current position.
    // If the state is on a specific node, we take its cheapest outgoing edge.
    // If it's a set of virtual nodes, we take the absolute minimum of all their cheapest edges.
    val initialCost: Int = state.position match {
      case TspNode(pos)        => cheapestEdges(pos)
      case VirtualNodes(nodes) => nodes.iterator.map(cheapestEdges).min
    }

    // Process all mandatory nodes (mustVisit).
    // We accumulate a tuple containing: (totalCost, maximumEdgeEncountered, shortestBackToDepot).
    // backToDepot is initialized to Infinity because the return to the depot will occur
    // from a future visited node, not from the current position.
    val mustResult: (Int, Int, Int) =
      state.mustVisit.foldLeft((initialCost, initialCost, Infinity)) {
        // If a mandatory node is unreachable, the tour is infeasible. We break and return Infinity.
        case (_, i) if !problem.reachable(state, i) => break(Infinity.toDouble)
        case ((cost, maxE, back), i)                =>
          (cost + cheapestEdges(i), maxE max cheapestEdges(i), back min problem.timeMatrix(i)(0))
      }

    // Calculate how many more optional nodes we need to select to complete a full tour.
    val numToCompleteTour: Int = numVar - state.depth - 1 - state.mustVisit.size

    // Process optional nodes (maybeVisit) if the tour is not yet complete.
    val (travelCost, maxEdge, backToDepot): (Int, Int, Int) = {
      if (numToCompleteTour > 0) {
        // Filter optional nodes to keep only the ones that are currently reachable.
        val candidatesNodes: List[Int] =
          state.maybeVisit.iterator.filter(problem.reachable(state, _)).toList

        // If there aren't enough reachable nodes to finish the tour, it's infeasible.
        if (candidatesNodes.length < numToCompleteTour) break(Infinity.toDouble)

        // Greedily select the required number of nodes with the cheapest outgoing edges.
        val selectedNodes: List[Int] = candidatesNodes.sortBy(cheapestEdges).take(numToCompleteTour)
        // Accumulate their costs on top of the results from the mandatory nodes.
        selectedNodes.foldLeft(mustResult) { case ((cost, maxE, back), i) =>
          (cost + cheapestEdges(i), maxE max cheapestEdges(i), back min problem.timeMatrix(i)(0))
        }
      } else mustResult
    }

    // Compute the total heuristic cost.
    // We subtract the maximum edge because a path visiting N nodes only uses N-1 edges.
    val total = {
      if (travelCost - maxEdge == 0) problem.minDuration(state, 0)
      else travelCost - maxEdge + backToDepot
    }

    // Final Time Window constraint check.
    // Ensure that adding this heuristic travel time to the current state time
    // does not violate the depot's closing time window.
    // Explicitly return a Double to satisfy Scala 3 typing constraints within the boundary block.
    if (state.time + total > problem.timeWindows(0).end) Infinity.toDouble
    else total.toDouble
  }

}
