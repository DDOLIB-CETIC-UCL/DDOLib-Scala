package org

/** Main package of the framework. It contains traits that bridge between Scala and the Java library
  * [[https://github.com/DDOLIB-CETIC-UCL/DDOLib DDOLib]]. It also defines a fully Scala user API.
  */
package object ddolibscala {

  // Allows to get access to the Solver factory without any import.
  import org.ddolibscala.solver.Solver
  export org.ddolibscala.solver.Solver

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/Solution.html org.ddolib.common.solver.Solution]]
    * object.
    */
  type Solution = org.ddolib.common.solver.Solution

  /** Alias for the
    * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/SearchStatistics.html org.ddolib.common.solver.SearchStatistics]]
    */
  type SearchStatistic = org.ddolib.common.solver.SearchStatistics

}
