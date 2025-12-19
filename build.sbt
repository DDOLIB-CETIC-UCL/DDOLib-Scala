ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.18"
ThisBuild / organization := "DDOLib-CETIC-UCL"

lazy val root = (project in file("."))
  .settings(name := "DDOLib-Scala")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xdisable-assertions")

libraryDependencies ++= Seq(
  "junit"              % "junit"           % "4.13.2"  % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.19.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % Test
)
