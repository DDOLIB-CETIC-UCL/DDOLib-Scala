package be.cetic.ddolibscala.common.solver.stopcriterion

import org.ddolib.common.solver.stopcriterion.DdoStopCriterion as JavaDdoStopCriterion

/** Factories for the stop criteria specific to the DDO-based solvers (DDO, exact, LNS). Given to
  * another solver, they throw an `IllegalArgumentException`.
  *
  * Wraps the factories of
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stopcriterion/DdoStopCriterion.html org.ddolib.common.solver.stopcriterion.DdoStopCriterion]].
  */
object DdoStopCriterion {

  /** Creates a stop criterion that terminates the search when the total number of MDD nodes
    * created exceeds a specified limit.
    *
    * @param maxNodes
    *   the maximum allowed number of MDD nodes
    */
  def maxTotalMddNodes(maxNodes: Long): StopCriterion =
    JavaDdoStopCriterion.maxTotalMddNodes(maxNodes).test(_)

  /** Creates a stop criterion that terminates the search when the number of iterations without any
    * lower bound improvement exceeds a specified limit.
    *
    * @param maxIter
    *   the maximum allowed number of iterations since the last lower bound improvement
    */
  def maxIterWithoutLowerBoundImprovement(maxIter: Int): StopCriterion =
    JavaDdoStopCriterion.maxIterWithoutLowerBoundImprovement(maxIter).test(_)

  /** Creates a stop criterion that terminates the search when the depth of the subproblem root
    * being explored (the node popped from the frontier) reaches or exceeds a specified limit.
    *
    * @param maxDepth
    *   the maximum allowed depth for a subproblem root
    */
  def maxExploredDepth(maxDepth: Int): StopCriterion =
    JavaDdoStopCriterion.maxExploredDepth(maxDepth).test(_)
}
