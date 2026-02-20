[![Tests](https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala/actions/workflows/ci.yml/badge.svg)](https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala/actions/workflows/ci.yml)
[![Scaladoc](https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala/actions/workflows/scaladoc.yml/badge.svg)](https://ddolib-cetic-ucl.github.io/DDOLib-Scala/scaladoc/index.html)

![](logo/logo.png)

# DDOLib-Scala

Scala API for [DDOLib](https://github.com/DDOLIB-CETIC-UCL/DDOLib) library.

[DDOLib](https://github.com/DDOLIB-CETIC-UCL/DDOLib) is a open-source (MIT) java solver for solving dynamic
programming (DP) problems developped
by the [CETIC](https://www.cetic.be/) (team of [Renaud De Landtsheer](https://www.cetic.be/Renaud-DE-LANDTSHEER)) and
the [UCLouvain](https://uclouvain.be/en/index.html) (team of [Pierre Schaus](https://pschaus.github.io/)).
It includes a modeling API for users to define their DP problem and solve them efficiently.

# Theoretical Foundations

The technique of using decision diagrams for solving combinatorial problems is based on the work of several researchers.

The first paper that introduced the concept of using decision diagrams for combinatorial optimization is:

*Bergman, D., Cire, A. A., Van Hoeve, W. J., & Hooker, J. N. (2016). Discrete optimization with decision diagrams.
INFORMS Journal on Computing.*

Then the technique was improved in several papers that are also implemented in this project or will eventually be:

* *Coppé, V., Gillard, X., & Schaus, P. (2024). Decision diagram-based branch-and-bound with caching for
  dominance and suboptimality detection. INFORMS Journal on Computing.*
* *Coppé, V., Gillard, X., & Schaus, P. (2024). Modeling and Exploiting Dominance Rules for Discrete Optimization with
  Decision Diagrams. CPAIOR.*
* *Gillard, X., & Schaus, P. (2022). Large Neighborhood Search with Decision Diagrams. In IJCAI.*
* *Gillard, X., Coppé, V., Schaus, P., & Cire, A. A. (2021). Improving the filtering of branch-and-bound MDD solver.
  CPAIOR*
* *Gillard, X., Schaus, P., & Coppé, V. (2014). Ddo, a generic and efficient framework for mdd-based optimization.
  IJCAI.*

# At the origin: The DDO Project

DDOLib is a Java version of DDO, which was originally implemented in Rust and is
available [here](https://github.com/xgillard/ddo).
If you use this Java version, please cite the DDO paper:

```bibtex
@inproceedings{gillard2014ddo,
  title={Ddo, a generic and efficient framework for mdd-based optimization},
  author={Gillard, X. and Schaus, P. and Copp{\'e}, V.},
  booktitle={IJCAI},
  year={2014}
}
```

## Examples

The project contains a set example models in the [example](./src/main/scala/org/ddolibscala/example) package.

The Maximum Independent Set Problem (MISP) a classical optimization problem on a graph.
In graph theory, an independent set is a set of vertices in a graph where
none of the vertices are adjacent to each other. The objective of the MISP is an independent set such that the sum of
weight of the selected nodes is maximal.

Several search strategies are available in DDOLib, and they share the same modeling interface. These include the classic
branch-and-bound (B&B) approach denoted Ddo, the Astar (A*) approach, and the Anytime Column Search (ACS).

Below is the implementation of the transition model for the MISP. The base idea of the transition model is the
following:

- A state is the set of the nodes that can be selected.
- If a node is selected, all its neighbors can be selected anymore.

```scala
class MispProblem(
                   nodes: BitSet,
                   val neighbors: Array[BitSet],
                   val weights: Array[Int],
                   _optimal: Option[Double]
                 ) extends Problem[BitSet] {

  private var name: Option[String] = None

  override def optimal: Option[Double] = _optimal.map(-_)

  override def nbVars(): Int = weights.length

  override def initialState(): BitSet = nodes

  override def initialValue(): Double = 0.0

  override def domainValues(state: BitSet, variable: Int): Iterable[Int] = {
    if (state.contains(variable)) List(0, 1)
    else List(0)
  }

  override def transition(state: BitSet, decision: Decision): BitSet = {
    val variable: Int = decision.`var`()
    if (decision.`val`() == 1) (state - variable) diff neighbors(variable)
    else state - variable
  }

  override def transitionCost(state: BitSet, decision: Decision): Double =
    -weights(decision.`var`()) * decision.`val`()
}
```

To the DDO solver, we have to implement a problem specific `Relaxation`. This relaxation is optimistic and aims to
define how to merge states.
For the MISP, merging states involves taking the union of the remaining nodes in each of the states.

```scala
class MispRelaxation extends Relaxation[BitSet] {

  override def merge(statesToMerge: Iterable[BitSet]): BitSet = statesToMerge.reduce(_ union _)

  override def relaxEdge(
                          from: BitSet,
                          to: BitSet,
                          merged: BitSet,
                          decision: Decision,
                          cost: Double
                        ): Double = cost
}
```

Solving a instance of the problem can be done as follows:

```scala
 def main(args: Array[String]): Unit = {

  val problem = MispProblem("data/MISP/50_nodes_1.dot")
  val solver: Solver =
    Solver.ddo(
      problem = problem,
      relaxation = MispRelaxation(),
      lowerBound = MispFlb(problem),
      widthHeuristic = FixedWidth(2),
      ranking = MispRanking(),
      frontier = Frontier,
      verbosityLvl = Large,
      useCache = true
    )

  val solution: Solution =
    solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
      println("------ NEW BEST ------")
      println(stats)
      println(sol.mkString("[", ", ", "]"))
    })

  println(solution)
  println(s"Search time: ${solution.statistics().runTimeMs()} ms")
}
```

You can find description of each parameter by reading
the [ScalaDoc](https://ddolib-cetic-ucl.github.io/DDOLib-Scala/scaladoc/index.html).

### Note

By default, DDOLib solves __minimization__ problems. For maximization problem as the MISP, we need ti make some
adaptation to convert it into a minimization problem.

### Recommended IDE: IntelliJ IDEA

We recommend using **IntelliJ IDEA** to develop and run the DDOLib project.

#### Steps to Import DDOLib into IntelliJ:

1. **Clone the Repository**:
   Open a terminal and run the following command to clone the repository:
   ```bash
   git clone https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala.git
    ```

2. **Clone the Repository**:
   Launch IntelliJ IDEA.
   Select File > Open and navigate to the DDOLib-Scala folder you cloned.
   Open the `build.sbt` file.

3. **Running the tests**:

   From the IntelliJ IDEA editor, navigate to the `src/test/scala` directory.
   Right-click then select `Run 'All Tests'` to run all the tests.

   From the terminal, navigate to the root directory of the project and run the following command:
    ```bash
    sbt test
    ```

## Acknowledgments

This project is funded by the Walloon Region (Belgium) as part of the Win4Collective project Convention 2410118.
We are grateful for their support and contribution to the development of this project.