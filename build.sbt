import ReleaseTransformations._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin, ReleasePlugin, ScalafmtPlugin)
  .settings(
    name := "sbt-netemera",
    organization := "com.netemera",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-unchecked"
    ),
    scalaVersion := "2.12.8",
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-nop" % "1.7.25"
    ),
    addSbtPlugin("com.github.mwegrz" %% "sbt-mwegrz" % "0.1.11"),
    // Release settings
    releaseTagName := { (version in ThisBuild).value },
    releaseTagComment := s"Release version ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("publishSigned"),
      setNextVersion,
      commitNextVersion,
      releaseStepCommandAndRemaining("sonatypeReleaseAll"),
      pushChanges
    ),
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    // Publish settings
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ =>
      false
    },
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("http://github.com/netemera/sbt-netemera")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/netemera/sbt-netemera.git"),
        "scm:git@github.com:mwegrz/sbt-netemera.git"
      )
    ),
    developers := List(
      Developer(
        id = "netemera",
        name = "Netemera",
        email = null,
        url = url("https://www.netemera.com")
      )
    )
  )
