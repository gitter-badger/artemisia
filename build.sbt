import sbt.Keys._

assemblySettings

lazy val ultron = (project in file(".")).enablePlugins(JavaAppPackaging)
  .settings(General.settings("ultron"))
  .settings(
  libraryDependencies ++= Ultron.dependencies
).dependsOn(commons,localhost)

lazy val localhost = (project in General.componentBase / "localhost").enablePlugins(JavaAppPackaging)
  .settings(General.settings("localhost")).dependsOn(commons)

lazy val commons = (project in General.subprojectBase / "commons").enablePlugins(JavaAppPackaging)
  .settings(General.settings("commons"))
  .settings(libraryDependencies ++= Commons.dependencies)




















