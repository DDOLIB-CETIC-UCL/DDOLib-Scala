package org.ddolibscala.util

import org.ddolib.util.verbosity.VerbosityLevel as JavaVerbosityLvl

/** Alias for the
  * [[https://ddolib-cetic-ucl.github.io/DDOLib/javadoc/org/ddolib/util/verbosity/VerbosityLevel.html org.ddolib.util.verbosity.VerbosityLevel]]
  */
enum VerbosityLvl {

  /** No output is produced.
    *
    * <p> This mode is intended for fully silent execution, where performance measurements or logs
    * are not required. </p>
    */
  case Silent

  /** Displays important progress updates.
    *
    * <p> In this mode, the solver prints a message each time a new best objective value is found
    * during the search. </p>
    */
  case Normal

  /** Displays detailed runtime information for debugging or analysis purposes.
    *
    * <p> In this mode, the solver outputs:
    *   - A message whenever a new best objective is found.
    *   - Periodic statistics about the search frontier (approximately every 0.5 seconds)
    *   - Information about each developed subproblem as the search progresses </p>
    *
    * This mode provides the highest level of detail and is useful for performance analysis and
    * algorithmic tuning.
    */
  case Large

  /** Same that [[Large]] but saves the logs into `logs.txt` file. */
  case Export

  /** Converts this enum into Java enum.*/
  def toJava: JavaVerbosityLvl = {
    this match {
      case Silent => JavaVerbosityLvl.SILENT
      case Normal => JavaVerbosityLvl.NORMAL
      case Large  => JavaVerbosityLvl.LARGE
      case Export => JavaVerbosityLvl.EXPORT
    }
  }
}
