import sbt.Keys._
import sbt._

object General {

  val mainScalaVersion = "2.11.7"
  val appVersion = "1.0"
  val subprojectBase = file("subprojects")
  val componentBase = subprojectBase / "components"
  val dependencies = Seq (
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "ch.qos.logback" % "logback-classic" % "0.9.28",
    "ch.qos.logback" % "logback-classic" % "0.9.28" % "runtime",
    "org.slf4j" % "slf4j-api" % "1.7.6" % "provided",
    "org.slf4j" % "slf4j-nop" % "1.7.6" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.pegdown" % "pegdown" % "1.0.2" % "test",
    "joda-time" % "joda-time" % "2.0",
    "com.typesafe" % "config" % "1.3.0"
  )
  val crossVersions =  Seq("2.10.4", "2.11.6")

  def settings(module: String) = Seq(
    name := module,
    version := General.appVersion,
    scalaVersion := General.mainScalaVersion,
    libraryDependencies ++= General.dependencies,
    crossScalaVersions := General.crossVersions,
    (dependencyClasspath in Test) <<= (dependencyClasspath in Test) map {
      _.filterNot(_.data.name.contains("logback-classic"))
    },
    resolvers += Resolver.jcenterRepo

  )

}

