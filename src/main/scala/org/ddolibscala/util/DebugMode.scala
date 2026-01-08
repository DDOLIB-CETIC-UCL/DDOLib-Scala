package org.ddolibscala.util

import org.ddolib.util.debug.DebugLevel as JavaDebugLvl

/** Alias for
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/util/debug/DebugLevel.html org.ddolib.util.debug.DebugLevel]]
  */
enum DebugMode {

  /** Disables all debugging features.
    *
    * <p> This mode is intended for production runs or benchmarking, where performance is
    * prioritized and no additional checks or debug information are generated. </p>
    */
  case On

  /** Enables extended debugging and diagnostic tools.
    *
    * <p> Includes all checks from `ON`, and adds: </p>
    *   - Export of failing or inconsistent decision diagrams (as <code>.dot</code> files) to assist
    *     with visualization and analysis.
    *   - Additional consistency verification of lower-bound computations (particularly useful for
    *     A*-based algorithms)
    *
    * This mode is recommended when investigating unexpected solver behavior or validating complex
    * model implementations.
    */
  case Off

  /** Enables extended debugging and diagnostic tools.
    *
    * <p> Includes all checks from `ON`, and adds: </p>
    *   - Export of failing or inconsistent decision diagrams (as <code>.dot</code> files) to assist
    *     with visualization and analysis.
    *   - Additional consistency verification of lower-bound computations (particularly useful for
    *     A*-based algorithms)
    *
    * This mode is recommended when investigating unexpected solver behavior or validating complex
    * model implementations.
    */
  case Extended

  /** Converts this enum into Java enum.*/
  def toJava: JavaDebugLvl = {
    this match {
      case On       => JavaDebugLvl.ON
      case Off      => JavaDebugLvl.OFF
      case Extended => JavaDebugLvl.EXTENDED
    }
  }

}