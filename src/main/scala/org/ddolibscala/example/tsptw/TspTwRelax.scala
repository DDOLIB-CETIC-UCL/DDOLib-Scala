package org.ddolibscala.example.tsptw

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Relaxation

import scala.collection.mutable

/** Companion object of the [[TspTwRelax]] class. */
object TspTwRelax {

  /** Returns an object that computes relaxation for [[TspTwState]].s
    *
    * @param numVar
    *   number of variables/nodes in the associated TSPTW problem
    * @return
    *   an object that computes relaxation for [[TspTwState]]
    */
  def apply(numVar: Int): TspTwRelax = new TspTwRelax(numVar)
}

/** Relaxation class for the Traveling Salesman Problem with Time Windows (TSPTW).
  *
  * <p> This class implements the [[org.ddolibscala.modeling.Relaxation]] interface for
  * [[TspTwState]]. It provides methods to merge multiple states into a relaxed state and to relax
  * the cost of transitions (edges) between states. </p>
  *
  * @param numVar
  *   number of variables/nodes in the associated TSPTW problem
  */
class TspTwRelax(numVar: Int) extends Relaxation[TspTwState] {

  /** Represents infinity for arrival time. */
  private val Infinity: Int = Int.MaxValue

  /** Merges multiple TSPTW states into a single relaxed state.
    *
    * <p> The merge operation consists of:</p>
    *   - Combining visited positions (union of all positions).
    *   - Computing the intersection of all "must visit" sets
    *   - Building the "possibly visit" set as the union of all "must visit" and "possibly visit"
    *     sets, then removing the nodes that are mandatory
    *   - Selecting the minimum arrival time among all states.
    *   - Keeping the depth of the last processed state.
    *
    * @param statesToMerge
    *   an iterator over the states to merge
    * @return
    *   a new TSPTWState representing the relaxed state
    */
  override def merge(statesToMerge: Iterable[TspTwState]): TspTwState = {
    val mergedPos: mutable.Set[Int] = mutable.HashSet.empty
    var mergedTime: Int             = Infinity
    val mergedMust: mutable.BitSet  = mutable.BitSet.empty
    val mergedMaybe: mutable.BitSet = mutable.BitSet.empty
    val mergedDepth: Int            = statesToMerge.head.depth

    for (state <- statesToMerge) {

      state.position match {
        case TspNode(pos)        => mergedPos += pos
        case VirtualNodes(nodes) => mergedPos ++= nodes
      }

      mergedMust &= state.mustVisit   // intersection
      mergedMaybe |= state.maybeVisit // union
      mergedMaybe |= state.mustVisit  // union

      mergedTime = mergedTime min state.time
    }

    mergedMaybe &~= mergedMust // diff

    TspTwState(
      VirtualNodes(Set.from(mergedPos)),
      mergedTime,
      mergedMust.toImmutable,
      mergedMaybe.toImmutable,
      mergedDepth
    )
  }

  /** Relaxes the cost of an edge (transition) between two states.
    *
    * <p> In this implementation, the cost is not modified, and the method simply returns the
    * provided value. This method can be extended to apply more sophisticated relaxations if needed.
    * </p>
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
  override def relaxEdge(
    from: TspTwState,
    to: TspTwState,
    merged: TspTwState,
    decision: Decision,
    cost: Double
  ): Double = cost
}
