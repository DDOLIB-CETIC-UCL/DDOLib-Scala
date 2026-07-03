package be.cetic.ddolibscala.common.frontier

import org.ddolib.layered.modeling.StateRanking

/** Factory for
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/SimpleFrontier.html SimpleFrontier]]
  */
object SimpleFrontier {

  /** Returns a simple frontier using a
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/CutSetType.html#LastExactLayer LastExactLayer cutset]]
    *
    * @see
    *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/SimpleFrontier.html SimpleFrontier]]
    * @param ranking
    *   the ordering used to determine which subproblem is most promising and should be explored
    *   first
    * @tparam T
    *   the type of states
    * @return
    *   a simple frontier using a `LastExactLayer` cutset
    */
  def lastExactLayer[T](
    ranking: StateRanking[T]
  ): org.ddolib.common.frontier.SimpleFrontier[T] =
    new org.ddolib.common.frontier.SimpleFrontier[T](
      ranking,
      org.ddolib.common.frontier.CutSetType.LastExactLayer
    )

  /** Returns a simple frontier using a
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/CutSetType.html#Frontier Frontier cutset]]
    *
    * @see
    *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/frontier/SimpleFrontier.html SimpleFrontier]]
    * @param ranking
    *   the ordering used to determine which subproblem is most promising and should be explored
    *   first
    * @tparam T
    *   the type of states
    * @return
    *   a simple frontier using a `Frontier` cutset
    */
  def apply[T](ranking: StateRanking[T]): org.ddolib.common.frontier.SimpleFrontier[T] =
    new org.ddolib.common.frontier.SimpleFrontier[T](
      ranking,
      org.ddolib.common.frontier.CutSetType.Frontier
    )

}
