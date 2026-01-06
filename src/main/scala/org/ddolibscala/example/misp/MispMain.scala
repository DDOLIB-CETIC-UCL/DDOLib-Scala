package org.ddolibscala.example.misp

import org.ddolib.common.solver.Solution
import org.ddolib.modeling.{FastLowerBound, Model, Problem, Solvers}

object MispMain {

  def main(args: Array[String]): Unit = {
    val p = MispProblem("data/MISP/weighted.dot")
    println(p)

    val model = new Model[Set[Int]] {
      override def problem(): Problem[Set[Int]] = p

      override def lowerBound(): FastLowerBound[Set[Int]] = MispFlb(p)
    }

    val solution: Solution = Solvers.minimizeAstar(model)

    println(solution)
  }

}
