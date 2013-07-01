import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "Upset"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.markdownj" % "markdownj" % "0.3.0-1.0.2b4",
    "org.eclipse.jgit" % "org.eclipse.jgit" % "2.3.1.201302201838-r",
    "org.gitective" % "gitective-core" % "0.9.9"
  )

  def customLessEntryPoints(base: File): PathFinder = (
    (base / "app" / "assets" / "bootstrap" / "less" * "bootstrap.less") +++
    (base / "app" / "assets" / "bootstrap" / "less" * "responsive.less") +++
    (base / "app" / "assets" / "stylesheets" * "*.less")
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    lessEntryPoints <<= ( baseDirectory(customLessEntryPoints) ),
    scalaVersion := "2.10.0",
    testOptions in Test += Tests.Argument("sequential", "true"),
    testOptions in Test += Tests.Argument("junitxml", "console")
  )

}
