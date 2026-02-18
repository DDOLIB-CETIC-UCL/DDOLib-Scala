package org.ddolibscala.util.testbench

import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.Using

/** Utility object designed to facilitate the loading of problem instances from the file system.
  *
  * This helper abstracts away the logic of listing files in a directory and handling I/O resources
  * safely.
  */
object ProblemLoader {

  /** Loads problem instances from all regular files located in a specific directory.
    *
    * It scans the provided directory path, filters for regular files (ignoring subdirectories), and
    * transforms each file path into a problem instance using the provided factory function. If the
    * directory does not exist or cannot be read, it returns an empty list.
    *
    * @tparam P
    *   the type of the problem instance to create.
    * @param pathDir
    *   the string path to the directory containing the problem files.
    * @param factory
    *   a function that creates a problem instance from a file path string.
    * @return
    *   a list of problem instances loaded from the directory.
    */
  def loadFromDir[P](pathDir: String)(factory: String => P): List[P] = {
    val dir = Paths.get(pathDir)
    if (Files.exists(dir)) {
      Using.resource(Files.walk(dir)) { stream =>
        stream // Java stream
          .filter(Files.isRegularFile(_))
          .map(file => factory(file.toString))
          .toList //to Java mutable List
          .asScala // to Scala mutable Buffer
          .toList // to Scala immutable List
      }
    } else Nil
  }
}
