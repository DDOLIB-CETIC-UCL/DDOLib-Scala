ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / organization := "DDOLib-CETIC-UCL"

lazy val root = (project in file("."))
  .settings(
    name := "DDOLib-Scala",
    assembly / assemblyJarName := s"${name.value}-${version.value}.jar",

    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    }
  )

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

libraryDependencies ++= Seq(
  "junit"              % "junit"           % "4.13.2"   % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.19.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)
