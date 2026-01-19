package org.ddolibscala.tools.ddo.heuristics.cluster

import org.ddolib.ddo.core.mdd.NodeSubProblem

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

/** Interface defining a strategy to reduce the number of nodes in a layer of a decision diagram by
  * clustering nodes for restriction and relaxation.
  *
  * <p> Implementations of this interface determine how to group nodes into clusters when the layer
  * exceeds a desired maximum width. All nodes assigned to clusters are removed from the original
  * layer.</p>
  *
  * @tparam T
  *   the type of states in the decision diagram
  */
trait ReductionStrategy[T] extends org.ddolib.ddo.core.heuristics.cluster.ReductionStrategy[T] {

  /** Generates clusters of nodes for restriction and relaxation from the given layer.
    *
    * <p> Each cluster is represented as a list of
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/ddo/core/mdd/NodeSubProblem.html NodeSubProblem]]
    * objects. All nodes included in clusters are removed from the input `layer`.
    *
    * @param layer
    *   the list of nodes at the current layer
    * @param maxWidth
    *   the target maximum width of the layer after reduction
    * @return
    *   an array of clusters, each cluster being a list of nodes
    */
  def cluster(layer: mutable.Buffer[NodeSubProblem[T]], maxWidth: Int): Seq[Seq[NodeSubProblem[T]]]

  /** Used by the solver. Converts the input and output of [[cluster]] from java to scala and vice
    * versa.
    */
  final override def defineClusters(
    layer: java.util.List[NodeSubProblem[T]],
    maxWidth: Int
  ): Array[java.util.List[NodeSubProblem[T]]] =
    cluster(layer.asScala, maxWidth).map(_.asJava).toArray
}
