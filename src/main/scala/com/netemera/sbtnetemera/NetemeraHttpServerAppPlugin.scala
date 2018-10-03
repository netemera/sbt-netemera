package com.netemera.sbtnetemera

import com.github.mwegrz.sbtlogback.LogbackPlugin
import com.github.mwegrz.sbtlogback.LogbackPlugin.autoImport._
import com.lucidchart.sbt.scalafmt.ScalafmtPlugin
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.GitPlugin.autoImport.git
import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import com.typesafe.sbt.SbtNativePackager
import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import org.scalastyle.sbt.ScalastylePlugin
import sbtrelease.ReleasePlugin.autoImport._
import sbt.Keys._
import sbt.{Setting, _}
import sbtrelease.ReleasePlugin
import spray.revolver.RevolverPlugin
import spray.revolver.RevolverPlugin.autoImport._
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import com.netemera.sbtnetemera.NetemeraLibraryDependencies._

import ReleaseTransformations._

import scala.language.implicitConversions

object NetemeraHttpServerAppPlugin extends AutoPlugin {
  override def requires: Plugins = ScalafmtPlugin && ScalastylePlugin && LogbackPlugin && ReleasePlugin &&
    GitVersioning && SbtNativePackager && JavaServerAppPackaging && DockerPlugin && RevolverPlugin

  override def trigger: PluginTrigger = noTrigger

  override lazy val projectConfigurations: Seq[Configuration] = Seq(IntegrationTest)

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    organization := "com.netemera",
    organizationName := "Netemera",
    developers := List(
      Developer(
        id = "netemera",
        name = "Netemera",
        email = "team@netemera.com",
        url = url("https://www.netemera.com")
      )
    ),

    scalacOptions in ThisBuild ++= Seq("-feature"),
    scalaVersion := NetemeraLibraryDependencies.Versions.Scala,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),

    scalafmtOnCompile := true,

    slf4jVersion := NetemeraLibraryDependencies.Versions.Slf4j,
    logbackVersion := NetemeraLibraryDependencies.Versions.Logback,

    resolvers += "Sonatype Maven Snapshots" at "https://oss.sonatype.org/content/repositories/releases",

    libraryDependencies ++= Seq(
      ThreetenExtra,
      ScalaTest % "test,it",
      ScalaCheck % "test,it",
      LogbackHocon,
      ScalaStructlog,
      ScalaUtil,
      Config,
      Ficus,

      ScalaApp,
      AkkaActor,
      AkkaStream,
      AkkaPersistence,
      AkkaPersistenceCassandra,
      AkkaPersistenceCassandraLauncher % "test,it",
      AkkaSlf4j,
      AkkaHttp,
      AkkaHttpTestkit % "test,it",
      AkkaHttpCirce,
      AkkaHttpAvro4s,
      JwtCirce,
      CirceCore,
      CirceGeneric,
      CirceGenericExtras,
      CirceParser,
      CirceJava8,
      Avro4sCore,
      KebsAvro
    ),

    releaseTagName := { (version in ThisBuild).value },
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
    releaseCrossBuild := true,

    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      setNextVersion,
      commitNextVersion,
      pushChanges
    ),

    crossPaths := true,
    autoScalaLibrary := true,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ =>
      false
    },

    git.useGitDescribe := true,

    offline := false,
    fork in run := true,
    connectInput in run := true,

    Revolver.enableDebugging(port = 5050, suspend = false),

    maintainer := "Netemera <team@netemera.com>",
    packageSummary := name.value,
    packageDescription := name.value,
    topLevelDirectory := None,

    dockerBaseImage := dockerRepository.value.map(_ + "/").getOrElse("") + "netemera/docker-java-jre:0.1.9",
    dockerRepository := Some("registry.gitlab.com"),
    dockerUsername := Some("netemera"),
    dockerUpdateLatest := true,
    dockerAlias := DockerAlias(
      dockerRepository.value,
      dockerUsername.value,
      (packageName in Docker).value,
      Some((version in Docker).value)
    ),
    dockerExposedPorts := Seq(8080)
  ) ++ Defaults.itSettings
}
