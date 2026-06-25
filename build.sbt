ThisBuild / scalaVersion := "3.8.4"
ThisBuild / organization := "be.cetic"
ThisBuild / versionScheme := Some("early-semver")

ThisBuild / homepage := Some(url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala"))
ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / developers := List(
  Developer(
    id    = "cetic",
    name  = "CETIC",
    email = "info@cetic.be",
    url   = url("https://www.cetic.be")
  )
)
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala"),
    "scm:git@github.com:DDOLIB-CETIC-UCL/DDOLib-Scala.git"
  )
)
ThisBuild / sonatypeCredentialHost := "central.sonatype.com"

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
  "org.scalacheck"    %% "scalacheck"      % "1.19.0"   % Test,
  "org.scalatest"     %% "scalatest"       % "3.2.19"   % Test,
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)  
