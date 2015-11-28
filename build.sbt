import sbt.Keys._

assemblySettings
name := """ultron"""
version := "1.0"
scalaVersion := "2.11.7"


lazy val finalTest = taskKey[Unit]("customized full test")

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging).settings (
  libraryDependencies ++= Dependencies.commonSettings,
  libraryDependencies ++= Dependencies.coreSettings,

  (dependencyClasspath in Test) <<= (dependencyClasspath in Test) map {
    _.filterNot(_.data.name.contains("logback-classic"))
  }

)




















