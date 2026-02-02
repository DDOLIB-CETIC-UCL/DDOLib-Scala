package org.ddolibscala.example.tsptw

import org.ddolib.ddo.core.Decision
import org.ddolibscala.modeling.Relaxation

import scala.collection.mutable


object TspTwRelax {

  def apply(numVar: Int): TspTwRelax = new TspTwRelax(numVar)
}

class TspTwRelax(numVar: Int) extends Relaxation[TspTwState] {

  private val Infinity: Int = Int.MaxValue

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

      mergedMust &= state.mustVisit // intersection
      mergedMaybe |= state.maybeVisit // union
      mergedMaybe |= state.mustVisit // union

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

  override def relaxEdge(
    from: TspTwState,
    to: TspTwState,
    merged: TspTwState,
    decision: Decision,
    cost: Double
  ): Double = cost
}
