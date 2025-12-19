package org.ddolibscala

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class ExampleTest extends AnyFunSuite with Matchers {

  test("Some test") {
    val lib = "DDOLib-Scala"
    lib.toLowerCase.contains("ddolib") must be(true)
  }

}
