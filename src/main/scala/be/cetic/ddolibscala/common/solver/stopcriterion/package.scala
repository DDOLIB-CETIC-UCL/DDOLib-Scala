package be.cetic.ddolibscala.common.solver {

  import be.cetic.ddolibscala.SearchStatistic

  /** This package contains factories of stop criteria deciding when a solver must terminate its
    * search, based on metrics such as time, iterations or lack of improvement.
    *
    * It wraps
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stopcriterion/package-summary.html org.ddolib.common.solver.stopcriterion]].
    * As in the Java library, a criterion specific to a solver family (see [[DdoStopCriterion]] and
    * [[AstarStopCriterion]]) throws an `IllegalArgumentException` if given to another family.
    *
    * Criteria can be combined:
    * {{{
    * import be.cetic.ddolibscala.common.solver.stopcriterion.*
    *
    * solver.minimize(limit = StopCriterion.maxTime(10000) || StopCriterion.minGap(1.0))
    * }}}
    */
  package object stopcriterion {

    /** A stop criterion returns `true` if the search should be terminated. It is the type of the
      * `limit` parameter of the solvers' `minimize` method.
      *
      * Alias for a Scala version of
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stopcriterion/StopCriterion.html org.ddolib.common.solver.stopcriterion.StopCriterion]].
      */
    type StopCriterion = SearchStatistic => Boolean

    extension (criterion: SearchStatistic => Boolean) {

      /** Returns a criterion that stops the search only when both criteria are met. */
      def &&(other: SearchStatistic => Boolean): StopCriterion =
        stats => criterion(stats) && other(stats)

      /** Returns a criterion that stops the search as soon as one of the criteria is met. */
      def ||(other: SearchStatistic => Boolean): StopCriterion =
        stats => criterion(stats) || other(stats)

      /** Returns the negation of the criterion. */
      def unary_! : StopCriterion = stats => !criterion(stats)
    }
  }

  package stopcriterion {}
}
