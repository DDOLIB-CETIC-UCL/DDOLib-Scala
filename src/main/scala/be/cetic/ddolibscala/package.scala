package be.cetic {

  /** Main package of the framework. It contains traits that bridge between Scala and the Java
    * library [[https://github.com/DDOLIB-CETIC-UCL/DDOLib DDOLib]]. It also defines a fully Scala
    * user API.
    */
  package object ddolibscala {}

  package ddolibscala {

    // Since we are inside be.cetic.ddolibscala, 'solver' is resolved relatively
    export solver.{NoLayerSolver, Solver}

    /** Alias for the
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solver/Solution.html org.ddolib.layered.solver.Solution]]
      * object.
      */
    type Solution = org.ddolib.layered.solver.Solution

    /** Alias for the
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solver/Solution.html org.ddolib.nolayer.solver.Solution]]
      * object.
      */
    type NoLayerSolution = org.ddolib.nolayer.solver.Solution

    /** Alias for the
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stat/SearchStatistics.html org.ddolib.common.solver.stat.SearchStatistics]]
      */
    type SearchStatistic = org.ddolib.common.solver.stat.SearchStatistics
  }
}
