package org.ddolibscala.example.misp

object MispMain {

  def main(args: Array[String]): Unit = {
    val problem = MispProblem("data/MISP/weighted.dot")
    println(problem)

  }

}
