package com.netemera.sbtnetemera

import sbt.Keys._
import sbt.{Setting, _}
import com.github.mwegrz.sbtmwegrz.MwegrzLibraryPlugin

import scala.language.implicitConversions

object NetemeraLibraryPlugin extends MwegrzLibraryPlugin {
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
    )
  )
}
