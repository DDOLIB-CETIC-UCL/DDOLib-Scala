package org.ddolibscala.example.misp

import org.ddolib.common.dominance.{DominanceChecker, SimpleDominanceChecker}
import org.ddolib.common.solver.Solution
import org.ddolib.modeling.{FastLowerBound, Model, Problem, Solvers}
import org.ddolib.util.io.SolutionPrinter

object MispAstarMain {

  def main(args: Array[String]): Unit = {
    val p = MispProblem("data/MISP/weighted.dot")
    println(p)

    val model = new Model[Set[Int]] {
      override def problem(): Problem[Set[Int]] = p

      override def lowerBound(): FastLowerBound[Set[Int]] = MispFlb(p)

      override def dominance(): DominanceChecker[Set[Int]] =
        new SimpleDominanceChecker[Set[Int]](MispDominance(), p.nbVars())
    }

    val solution: Solution =
      Solvers.minimizeAstar(model, (sol, s) => SolutionPrinter.printSolution(s, sol))

    println(solution)
  }

}
