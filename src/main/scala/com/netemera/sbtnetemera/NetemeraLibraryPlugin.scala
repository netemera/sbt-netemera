package com.netemera.sbtnetemera

import sbt.Keys._
import sbt.{ Setting, _ }
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
    ),
    credentials ++= sys.env
      .get("MY_MAVEN_REPO_PASSWORD")
      .fold(Seq.empty[Credentials])(password =>
        Seq(
          Credentials("mymavenrepo.com.read", "mymavenrepo.com", "myMavenRepo", password),
          Credentials("mymavenrepo.com.write", "mymavenrepo.com", "myMavenRepo", password)
        )
      ),
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      "MyMavenRepo read" at "https://mymavenrepo.com/repo/zghBEmhHgl0OkEqxC1RO"
    ),
    publishTo := Some("MyMavenRepo write" at "https://mymavenrepo.com/repo/dWLBAjbgqRQN6dpgLsm5")
  )
}
