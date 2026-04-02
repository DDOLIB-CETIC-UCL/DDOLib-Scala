ThisBuild / version      := "0.0.4-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.4"
ThisBuild / organization := "be.cetic"

lazy val root = (project in file("."))
  .settings(
    name := "DDOLib-Scala",

    assembly / assemblyJarName := s"${name.value}-${version.value}.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x                             => MergeStrategy.first
    },

    description := "DDOLib Scala library",
    homepage := Some(url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala")),

    licenses := List("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),

    developers := List(
      Developer(
        name = "Quentin Meurisse",
        email = "quentin.meurisse@cetic.be",
      )
    ),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/DDOLIB-CETIC-UCL/DDOLib-Scala"),
        "scm:git@github.com:DDOLIB-CETIC-UCL/DDOLib-Scala.git"
      )
    )
  )

// Compiler options
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Dependencies
libraryDependencies ++= Seq(
  "junit"              % "junit"           % "4.13.2"   % Test,
  "org.scalacheck"    %% "scalacheck"      % "1.19.0",
  "org.scalatest"     %% "scalatest"       % "3.2.19",
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)

// Sonatype
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

// GPG signing
ThisBuild / useGpg := true
ThisBuild / pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray)
ThisBuild / pgpCmd := "gpg"

// Do not publish test artifacts
Test / publishArtifact := false