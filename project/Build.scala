import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Upset"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )

  def customLessEntryPoints(base: File): PathFinder = (
      (base / "app" / "assets" / "bootstrap" / "less" * "bootstrap.less") +++
      (base / "app" / "assets" / "bootstrap" / "less" * "responsive.less") +++
      (base / "app" / "assets" / "stylesheets" * "*.less")
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
    lessEntryPoints <<= baseDirectory(customLessEntryPoints)
  )

}
