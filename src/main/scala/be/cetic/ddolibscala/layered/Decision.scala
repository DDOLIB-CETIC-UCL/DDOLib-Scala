package be.cetic.ddolibscala.layered

import org.ddolib.layered.solving.ddo.core.Decision as JavaDecision

/** Packaging for
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/layered/solving/ddo/core/Decision.html Decision]]
  * A `Decision` associates a variable identifier with a specific value. It is an immutable data
  * structure that captures a single assignment performed during the search or compilation of a
  * decision diagram (e.g., in an MDD-based solver).
  */
object Decision {

  /** Returns a decision assigning the input value the variable at the given index.
    *
    * @param variable
    *   the index of the variable being assigned
    * @param value
    *   the value assigned to the variable
    *
    * @return
    *   a decision assigning the input value the variable at the given index
    */
  def apply(variable: Int, value: Int): JavaDecision = {
    new JavaDecision(variable, value)
  }

}
