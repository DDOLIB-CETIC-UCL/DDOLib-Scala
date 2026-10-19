package be.cetic.ddolibscala.common.solver.stopcriterion

import org.ddolib.common.solver.stopcriterion.AstarStopCriterion as JavaAstarStopCriterion

/** Factories for the stop criteria specific to the A*-based solvers (A*, Anytime Weighted A*,
  * ACS). Given to another solver, they throw an `IllegalArgumentException`.
  *
  * Wraps the factories of
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stopcriterion/AstarStopCriterion.html org.ddolib.common.solver.stopcriterion.AstarStopCriterion]].
  */
object AstarStopCriterion {

  /** Creates a stop criterion that terminates the search when the smoothed percentage of valid
    * children falls below or equal to a specified threshold.
    *
    * @param minPercent
    *   the minimum required percentage of valid children to continue
    */
  def minValidChildrenPercent(minPercent: Double): StopCriterion =
    JavaAstarStopCriterion.minValidChildrenPercent(minPercent).test(_)
}
