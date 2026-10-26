package be.cetic.ddolibscala

import be.cetic.ddolibscala.layered.modeling.DefaultDominanceChecker
import org.ddolib.layered.modeling.DominanceChecker

import scala.jdk.FunctionConverters.enrichAsJavaPredicate

// Explicit package aliases renamed for style consistency
import be.cetic.ddolibscala.layered.{modeling as layeredModeling, solver as layeredSolver}
import be.cetic.ddolibscala.nolayer.{modeling as noLayerModeling, solver as noLayerSolver}

/** Unified factory for the start heuristics, which quickly compute a first solution. This solution
  * can then be given as `initialSolution` to a solver of [[Solvers]].
  */
object StartHeuristics {

  object layered {

    /** Runs the layered
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/startheuristic/DFS.html DFS start heuristic]]
      * and returns the first feasible solution it encounters.
      *
      * The children of a node are explored by increasing `g + h`, where `h` is given by the fast
      * lower bound. States dominated by an already explored state are skipped.
      *
      * @param problem
      *   the problem to solve
      * @param lowerBound
      *   a heuristic that estimates a lower bound on the objective value for a given state
      * @param dominance
      *   the dominance checker used to skip dominated states
      * @param limit
      *   stopping criterion tested at each iteration; its statistics are the ones of A*, see
      *   [[be.cetic.ddolibscala.common.solver.stopcriterion.AstarStopCriterion]]
      * @tparam T
      *   the type representing a state in the problem
      * @return
      *   the first solution found, or an empty solution (see `isEmpty`) if the problem has no
      *   solution or if the limit was reached first
      */
    def dfs[T](
      problem: org.ddolib.layered.modeling.Problem[T],
      lowerBound: org.ddolib.layered.modeling.FastLowerBound[T] =
        new layeredModeling.DefaultFastLowerBound[T](),
      dominance: DominanceChecker[T] = DefaultDominanceChecker[T](),
      limit: SearchStatistic => Boolean = _ => false
    ): layeredSolver.Solution = {
      val _problem    = problem
      val _lowerBound = lowerBound
      val _dominance  = dominance

      // DFS only reads the problem, the lower bound and the dominance of the model.
      val model = new org.ddolib.layered.modeling.Model[T] {
        override def problem(): org.ddolib.layered.modeling.Problem[T] = _problem

        override def lowerBound(): org.ddolib.layered.modeling.FastLowerBound[T] = _lowerBound

        override def dominance(): DominanceChecker[T] = _dominance
      }

      val dfs = new org.ddolib.layered.solving.startheuristic.DFS[T](model)
      new layeredSolver.Solution(dfs.findSolution(limit.asJava))
    }
  }

  object nolayer {

    /** Runs the NoLayer
      * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/nolayer/solving/startheuristic/DFS.html DFS start heuristic]]
      * and returns the first feasible solution it encounters.
      *
      * The children of a node are explored by increasing `g + h`, where `h` is given by the fast
      * lower bound. States already explored, or dominated by an explored state, are skipped.
      *
      * @param problem
      *   the problem to solve
      * @param lowerBound
      *   a heuristic that estimates a lower bound on the objective value for a given state
      * @param dominance
      *   the dominance checker used to skip dominated states
      * @param limit
      *   stopping criterion tested at each iteration; its statistics are the ones of A*, see
      *   [[be.cetic.ddolibscala.common.solver.stopcriterion.AstarStopCriterion]]
      * @tparam T
      *   the type representing a state in the problem
      * @return
      *   the first solution found, or an empty solution (see `isEmpty`) if the problem has no
      *   solution or if the limit was reached first
      */
    def dfs[T](
      problem: noLayerModeling.Problem[T],
      lowerBound: noLayerModeling.FastLowerBound[T] =
        new noLayerModeling.DefaultFastLowerBound[T](),
      dominance: noLayerModeling.NoLayerDominanceChecker[T] =
        new noLayerModeling.DefaultNoLayerDominanceChecker[T](),
      limit: SearchStatistic => Boolean = _ => false
    ): noLayerSolver.Solution = {
      val _problem    = problem
      val _lowerBound = lowerBound
      val _dominance  = dominance

      // DFS only reads the problem, the lower bound and the dominance of the model.
      val model = new org.ddolib.nolayer.modeling.Model[T] {
        override def problem(): org.ddolib.nolayer.modeling.Problem[T] = _problem

        override def lowerBound(): org.ddolib.nolayer.modeling.FastLowerBound[T] = _lowerBound

        override def dominance(): org.ddolib.nolayer.modeling.NoLayerDominanceChecker[T] =
          _dominance
      }

      val dfs = new org.ddolib.nolayer.solving.startheuristic.DFS[T](model)
      new noLayerSolver.Solution(dfs.findSolution(limit.asJava))
    }
  }

}
