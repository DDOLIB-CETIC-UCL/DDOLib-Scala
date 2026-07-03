package be.cetic {

  /** Main package of the framework. It contains traits that bridge between Scala and the Java
    * library [[https://github.com/DDOLIB-CETIC-UCL/DDOLib DDOLib]]. It also defines a fully Scala
    * user API.
    */
  package object ddolibscala {}

  package ddolibscala {

    /** Alias for the
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/stat/SearchStatistics.html org.ddolib.common.solver.stat.SearchStatistics]]
      */
    type SearchStatistic = org.ddolib.common.solver.stat.SearchStatistics
  }
}
