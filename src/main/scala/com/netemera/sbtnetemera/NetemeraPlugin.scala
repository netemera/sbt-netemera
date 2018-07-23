package com.netemera.sbtnetemera
import sbt._

import scala.language.implicitConversions

object NetemeraPlugin extends AutoPlugin {
  object autoImport {
    lazy val NetemeraLibraryDependencies: com.netemera.sbtnetemera.NetemeraLibraryDependencies.type = com.netemera.sbtnetemera.NetemeraLibraryDependencies
  }
}

