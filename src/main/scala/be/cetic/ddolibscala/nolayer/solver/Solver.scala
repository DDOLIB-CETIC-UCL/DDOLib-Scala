package be.cetic.ddolibscala.nolayer.solver

import be.cetic.ddolibscala.SearchStatistic

import scala.jdk.CollectionConverters.ListHasAsScala
import scala.jdk.FunctionConverters.{enrichAsJavaBiConsumer, enrichAsJavaPredicate}

/** Class packaging a Java NoLayer solver
  */
class Solver private[ddolibscala] (javaSolver: org.ddolib.nolayer.solver.Solver) {

  def minimize(
    limit: SearchStatistic => Boolean = _ => false,
    onSolution: (Seq[Int], SearchStatistic) => Unit = (_, _) => {}
  ): Solution = {
    val onSolutionJava = (solution: java.util.List[java.lang.Integer], stats: SearchStatistic) =>
      onSolution(solution.asScala.toSeq.map(_.toInt), stats)
    
    Solution(javaSolver.minimize(limit.asJava, onSolutionJava.asJava))
  }

}
