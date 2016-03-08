import sbt.Keys._

assemblySettings

lazy val ultron = (project in file(".")).enablePlugins(JavaAppPackaging)
  .settings(General.settings("ultron"))
  .settings(
  libraryDependencies ++= Ultron.dependencies
).dependsOn(commons % "compile->compile;test->test",localhost)

lazy val localhost = (project in General.componentBase / "localhost").enablePlugins(JavaAppPackaging)
  .settings(General.settings("localhost")).dependsOn(commons  % "compile->compile;test->test")

lazy val commons = (project in General.subprojectBase / "commons").enablePlugins(JavaAppPackaging)
  .settings(General.settings("commons"))
  .settings(libraryDependencies ++= Commons.dependencies)


lazy val all = project.aggregate(ultron, localhost, commons).enablePlugins(JavaAppPackaging)
















