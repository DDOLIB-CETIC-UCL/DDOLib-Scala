ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / organization := "DDOLib-CETIC-UCL"

lazy val root = (project in file("."))
  .settings(name := "DDOLib-Scala")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")


val javafxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown OS")
}

val javaFxVersion = "21.0.2"
val scalaFxVersion = "21.0.0-R32"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % scalaFxVersion, // Version compatible Scala 3
) ++ javafxModules.map(m => "org.openjfx" % s"javafx-$m" % javaFxVersion classifier osName)

libraryDependencies ++= Seq(
  "junit"              % "junit"           % "4.13.2"  % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.19.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)

fork := true