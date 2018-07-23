package com.netemera.sbtnetemera

import com.github.mwegrz.sbtlogback.LogbackPlugin
import com.github.mwegrz.sbtlogback.LogbackPlugin.autoImport._
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import com.lucidchart.sbt.scalafmt.ScalafmtPlugin
import com.netemera.sbtnetemera.NetemeraLibraryDependencies._
import com.typesafe.sbt.GitPlugin.autoImport.git
import com.typesafe.sbt.GitVersioning
import org.scalastyle.sbt.ScalastylePlugin
import sbt.Keys._
import sbt.{Setting, _}
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import ReleaseTransformations._
import com.typesafe.sbt.pgp.PgpKeys

import scala.language.implicitConversions

object NetemeraApache2LibraryPlugin extends AutoPlugin {
  override def requires: Plugins = ScalafmtPlugin && ScalastylePlugin && LogbackPlugin && ReleasePlugin &&
    GitVersioning

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
      Ficus
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
      releaseStepCommand("publishSigned"),
      setNextVersion,
      commitNextVersion,
      releaseStepCommand("sonatypeRelease"),
      pushChanges
    ),
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("http://github.com/netemera/netemera-scala-client")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/netemera/netemera-scala-client.git"),
        "scm:git@github.com:netemera/netemera-scala-client.git"
      )
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
    connectInput in run := true
  ) ++ Defaults.itSettings
}
