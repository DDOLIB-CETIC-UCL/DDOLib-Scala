package org.ddolibscala.example.misp

import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Line}
import scalafx.scene.text.Text
// DDO Imports
import org.ddolibscala.tools.ddo.heuristics.width.FixedWidth
import org.ddolibscala.{SearchStatistic, Solver}

import scala.collection.mutable

object MispApp extends JFXApp3 {

  // --- Configuration ---
  val WindowWidth  = 800
  val WindowHeight = 800
  val NodeRadius   = 15
  val LayoutRadius = 350

  // Store visual references
  val nodeCircles = mutable.Map[Int, Circle]()

  override def start(): Unit = {

    // 1. Load Problem
    val problem  = MispProblem("data/MISP/weighted.dot")
    val numNodes = problem.nbVars()

    // 2. Setup Pane
    val rootPane = new Pane {
      style = "-fx-background-color: #f0f0f0;"
    }

    // --- Calculate Positions ---
    // Renamed variables to avoid ambiguity with ScalaFX properties
    val paneCenterX = WindowWidth / 2
    val paneCenterY = WindowHeight / 2
    val angleStep   = 2 * Math.PI / numNodes // Fixed: Math.PI

    val nodePositions: Map[Int, (Double, Double)] = (0 until numNodes).map { i =>
      val angle = i * angleStep
      val calcX = paneCenterX + LayoutRadius * Math.cos(angle)
      val calcY = paneCenterY + LayoutRadius * Math.sin(angle)
      i -> (calcX, calcY)
    }.toMap

    // 3. Draw Edges
    for (i <- 0 until numNodes) {
      val neighbors = problem.neighbors(i)
      // Renamed to nodeX/nodeY to avoid ambiguity
      val (nodeX1, nodeY1) = nodePositions(i)

      neighbors.foreach { neighbor =>
        if (neighbor > i) {
          val (nodeX2, nodeY2) = nodePositions(neighbor)
          val line             = new Line {
            startX = nodeX1
            startY = nodeY1
            endX = nodeX2
            endY = nodeY2
            stroke = Color.LightGray
            strokeWidth = 1
          }
          rootPane.children.add(line)
        }
      }
    }

    // 4. Draw Nodes
    for (i <- 0 until numNodes) {
      val (nodeX, nodeY) = nodePositions(i) // Renamed variables

      val circle = new Circle {
        centerX = nodeX // Now clear: Property = Value
        centerY = nodeY
        radius = NodeRadius
        fill = Color.White
        stroke = Color.Black
        strokeWidth = 2
      }

      val label = new Text {
        text = s"$i"
        // Use nodeX/nodeY to avoid ambiguity with Text's own x/y properties
        layoutX = nodeX - 5
        layoutY = nodeY + 5
      }

      rootPane.children.addAll(circle, label)
      nodeCircles(i) = circle
    }

    // 5. Scene
    stage = new JFXApp3.PrimaryStage {
      title = "MISP Visualization with DDO"
      scene = new Scene(rootPane, WindowWidth, WindowHeight)
    }

    // 6. Solver Thread
    val solverThread = new Thread(() => {

      println("Starting solver...")

      val solver = Solver.ddo(
        problem = problem,
        relaxation = MispRelaxation(),
        lowerBound = MispFlb(problem),
        widthHeuristic = FixedWidth(2),
        ranking = MispRanking()
      )

      val solution = solver.minimize(onSolution = (sol: Array[Int], stats: SearchStatistic) => {
        Platform.runLater {
          updateVisualization(sol)
          Thread.sleep(500)
        }
      })

      println(s"Search finished. Time: ${solution.statistics().runTimeMs()} ms")
    })

    solverThread.setDaemon(true)
    solverThread.start()
  }

  def updateVisualization(solution: Array[Int]): Unit = {
    solution.zipWithIndex.foreach { case (selected, index) =>
      nodeCircles.get(index).foreach { circle =>
        if (selected == 1) {
          circle.fill = Color.LightGreen
        } else {
          circle.fill = Color.White
        }
      }
    }
  }
}
