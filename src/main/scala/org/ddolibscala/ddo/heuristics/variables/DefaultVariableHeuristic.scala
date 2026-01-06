package org.ddolibscala.ddo.heuristics.variables

object DefaultVariableHeuristic {
  def apply[T](): DefaultVariableHeuristic[T] = new DefaultVariableHeuristic()

}

class DefaultVariableHeuristic[T] extends VariableHeuristic[T] {

  override def next(variables: Iterable[Int], states: Iterable[T]): Int = variables.head
}
