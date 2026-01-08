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
  case SILENT

  /** Displays important progress updates.
    *
    * <p> In this mode, the solver prints a message each time a new best objective value is found
    * during the search. </p>
    */
  case NORMAL

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
  case LARGE

  /** Same that [[LARGE]] but saves the logs into `logs.txt` file. */
  case EXPORT

  /** Converts this enum into Java enum.*/
  def toJava: JavaVerbosityLvl = {
    this match {
      case SILENT => JavaVerbosityLvl.SILENT
      case NORMAL => JavaVerbosityLvl.NORMAL
      case LARGE  => JavaVerbosityLvl.LARGE
      case EXPORT => JavaVerbosityLvl.EXPORT
    }
  }
}
