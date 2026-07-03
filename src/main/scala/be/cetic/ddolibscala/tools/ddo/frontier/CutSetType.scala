package be.cetic.ddolibscala.tools.ddo.frontier

import org.ddolib.common.frontier.CutSetType as JavaCutSetType

/** Alias for
  *
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/CutSetType.html org.ddolib.ddo.core.frontier.CutSetType]]
  */
enum CutSetType {

  /** The cut set corresponds to the last layer of the search space that can be evaluated exactly
    * before approximations are applied.
    */
  case LastExactLayer

  /** The cut set is defined by the current search frontier — the set of nodes that are candidates
    * for expansion.
    */
  case Frontier

  /** Converts this enum into Java enum. */
  def toJava: JavaCutSetType = {
    this match {
      case LastExactLayer => JavaCutSetType.LastExactLayer
      case Frontier       => JavaCutSetType.Frontier
    }
  }
}
