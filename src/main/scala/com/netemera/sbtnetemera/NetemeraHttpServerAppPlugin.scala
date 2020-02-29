package com.netemera.sbtnetemera

import com.typesafe.sbt.SbtNativePackager.autoImport._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.Keys.{ resolvers, _ }
import sbt.{ Setting, _ }
import com.github.mwegrz.sbtmwegrz.MwegrzHttpServerAppPlugin

import scala.language.implicitConversions

object NetemeraHttpServerAppPlugin extends MwegrzHttpServerAppPlugin {
  override lazy val projectSettings: Seq[Setting[_]] = super.projectSettings ++ Seq(
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
    resolvers += "MyMavenRepo read" at "https://mymavenrepo.com/repo/dWLBAjbgqRQN6dpgLsm5",
    maintainer := "Netemera <team@netemera.com>",
    dockerRepository := Some("registry.gitlab.com"),
    dockerUsername := Some("netemera")
  )
}
