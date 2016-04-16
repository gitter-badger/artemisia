import UnidocKeys._
import com.typesafe.sbt.SbtGit.GitKeys._
import sbt._


assemblySettings

lazy val ultron = (project in file(".")).enablePlugins(JavaAppPackaging)
  .settings(General.settings("ultron"))
  .settings(
  libraryDependencies ++= Ultron.dependencies
).dependsOn(commons % "compile->compile;test->test",localhost, mysql)

lazy val localhost = (project in General.componentBase / "localhost").enablePlugins(JavaAppPackaging)
  .settings(General.settings("localhost")).dependsOn(commons  % "compile->compile;test->test")

lazy val commons = (project in General.subprojectBase / "commons").enablePlugins(JavaAppPackaging)
  .settings(General.settings("commons"))
  .settings(libraryDependencies ++= Commons.dependencies)

lazy val mysql = (project in General.componentBase / "database" / "mysql").enablePlugins(JavaAppPackaging)
  .settings(General.settings("mysql")).dependsOn(commons  % "compile->compile;test->test")
  .settings(Modules.MySQL.settings)


lazy val all = project.aggregate(ultron, localhost, mysql, commons).enablePlugins(JavaAppPackaging)
  .settings(unidocSettings)
  .settings(site.settings ++ ghpages.settings: _*)
  .settings(
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(ultron),
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "latest/api"),
    gitRemoteRepo := "git@github.com:mig-foxbat/ultron.git"
  )
















