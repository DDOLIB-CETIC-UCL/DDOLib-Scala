package be.cetic.ddolibscala.example.misp.nolayer

import be.cetic.ddolibscala.modeling.nolayer.NoLayerDominanceChecker

import scala.collection.immutable.BitSet
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MispNoLayerDominance(numTotalNode: Int) extends NoLayerDominanceChecker[BitSet] {

  private case class DominantState(remainingNodes: BitSet, value: Double)

  private val dominanceFronts: mutable.Map[Int, ListBuffer[DominantState]] = mutable.Map()

  override def updateDominance(state: BitSet, value: Double): Boolean = {
    val isDominated = (state.size to numTotalNode).exists { card =>
      dominanceFronts.get(card).exists { front =>
        front.exists(entry => entry.value <= value && state.subsetOf(entry.remainingNodes))
      }
    }

    if (isDominated) {
      true
    } else {
      dominanceFronts
        .getOrElseUpdate(state.size, ListBuffer.empty)
        .addOne(DominantState(state, value))

      for (card <- 0 to state.size) {
        dominanceFronts.get(card).foreach { front =>
          front.filterInPlace { entry =>
            !(value <= entry.value && entry.remainingNodes.subsetOf(state))
          }
        }
      }
      false
    }
  }

  override def clear(): Unit = dominanceFronts.clear()
}
