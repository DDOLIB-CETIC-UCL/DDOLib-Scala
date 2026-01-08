package org.ddolibscala
package solver

import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

/** Class packaging a solver
  *
  * @param javaSolver
  *   a solver from java version of DDOLib
  */
class Solver(javaSolver: org.ddolib.common.solver.Solver) {

  /** Minimizes the objective function according to the solver strategy.
    *
    * <p> It converts input and output from java to scala and vice versa </p>
    *
    * @param limit
    *   a predicate that can limit or stop the search based on current
    *   [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/common/solver/SearchStatistics.html SearchStatistics]]
    * @param onSolution
    *   invoked on each new solution found; receives the solution array and current statistics
    * @return
    *   the solution obtained after the search
    */
  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Array[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    javaSolver.minimize(limit.asJava, onSolution.asJava)
  }

}
