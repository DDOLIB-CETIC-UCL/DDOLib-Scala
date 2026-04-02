ThisBuild / version := "0.0.5"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / organization := "be.cetic"

lazy val root = (project in file("."))
  .settings(
    name := "DDOLib-Scala",

    assembly / assemblyJarName :=
      s"${name.value}-${version.value}.jar",

    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _                             => MergeStrategy.first
    }
  )


libraryDependencies ++= Seq(
  "junit"              % "junit"           % "4.13.2"   % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.19.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)

ThisBuild / licenses := List(
  "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")
)

ThisBuild / homepage := Some(
  url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala")
)

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala"),
    "scm:git@github.com:DDOLIB-CETIC-UCL/DDOLib-Scala.git"
  )
)
