package be.cetic.ddolibscala.layered.solver

import be.cetic.ddolibscala.SearchStatistic

import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

/** Class packaging a Java layered solver
  */
class Solver private[ddolibscala] (javaSolver: org.ddolib.layered.solver.Solver) {

  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Array[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    javaSolver.minimize(limit.asJava, onSolution.asJava)
  }

}
