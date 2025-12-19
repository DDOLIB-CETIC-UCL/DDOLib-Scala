package org.ddolibscala

import org.ddolib.examples.max2sat.BinaryClause
object Main {
  def main(args: Array[String]): Unit = {

    val bc = new BinaryClause(1, 2)
    println(bc)
  }
}
