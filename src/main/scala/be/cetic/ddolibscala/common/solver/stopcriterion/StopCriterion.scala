package be.cetic.ddolibscala.common.solver.stopcriterion

import org.ddolib.common.solver.stopcriterion.StopCriterion as JavaStopCriterion

/** Factories for the stop criteria applicable to any solver.
  *
  * Wraps the factories of
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stopcriterion/StopCriterion.html org.ddolib.common.solver.stopcriterion.StopCriterion]].
  */
object StopCriterion {

  /** Creates a stop criterion that terminates the search when the total runtime exceeds a
    * specified limit.
    *
    * @param maxTimeMs
    *   the maximum allowed runtime in milliseconds
    */
  def maxTime(maxTimeMs: Long): StopCriterion =
    JavaStopCriterion.maxTime(maxTimeMs).test(_)

  /** Creates a stop criterion that terminates the search when the number of iterations exceeds a
    * specified limit.
    *
    * @param maxIter
    *   the maximum allowed number of iterations
    */
  def maxIter(maxIter: Int): StopCriterion =
    JavaStopCriterion.maxIter(maxIter).test(_)

  /** Creates a stop criterion that terminates the search if no improvement has been found for a
    * certain amount of time.
    *
    * @param maxTimeMs
    *   the maximum allowed time since the last improvement in milliseconds
    */
  def maxTimeSinceLastImprovement(maxTimeMs: Long): StopCriterion =
    JavaStopCriterion.maxTimeSinceLastImprovement(maxTimeMs).test(_)

  /** Creates a stop criterion that terminates the search if no improvement has been found for a
    * certain number of iterations.
    *
    * @param maxIter
    *   the maximum allowed number of iterations since the last improvement
    */
  def maxIterSinceLastImprovement(maxIter: Int): StopCriterion =
    JavaStopCriterion.maxIterSinceLastImprovement(maxIter).test(_)

  /** Creates a stop criterion that terminates the search when the relative improvement between the
    * current incumbent and the previous incumbent is less than or equal to a threshold.
    *
    * @param threshold
    *   the minimum relative improvement required to continue the search in percent
    */
  def minRelativeImprovement(threshold: Double): StopCriterion =
    JavaStopCriterion.minRelativeImprovement(threshold).test(_)

  /** Creates a stop criterion that terminates the search when the optimality gap is less than or
    * equal to a specified threshold.
    *
    * @param gapPercent
    *   the maximum allowed optimality gap in percent
    */
  def minGap(gapPercent: Double): StopCriterion =
    JavaStopCriterion.minGap(gapPercent).test(_)

  /** Creates a stop criterion that terminates the search when the frontier size exceeds a
    * specified limit.
    *
    * @param maxNodes
    *   the maximum allowed size of the frontier
    */
  def maxFrontierSize(maxNodes: Int): StopCriterion =
    JavaStopCriterion.maxFrontierSize(maxNodes).test(_)

  /** Creates a stop criterion that terminates the search when the number of iterations without any
    * gap improvement exceeds a specified limit.
    *
    * @param maxIter
    *   the maximum allowed number of iterations since the last gap improvement
    */
  def maxIterWithoutGapImprovement(maxIter: Int): StopCriterion =
    JavaStopCriterion.maxIterWithoutGapImprovement(maxIter).test(_)
}
