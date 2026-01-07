package org.ddolibscala
package solver

import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

class Solver(javaSolver: org.ddolib.common.solver.Solver) {

  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Array[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    javaSolver.minimize(limit.asJava, onSolution.asJava)
  }

}
