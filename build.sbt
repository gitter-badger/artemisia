import com.typesafe.sbt.SbtGit.GitKeys._
import sbt._
import sbtunidoc.Plugin.UnidocKeys._
import Modules._

assemblySettings



//addCommandAlias("full-test", "clean;test;coverageReport")


lazy val ultron = (project in file(".")).enablePlugins(JavaAppPackaging)
  .settings(General.settings("ultron"))
  .settings(
  libraryDependencies ++= Ultron.dependencies
).dependsOn(commons % "compile->compile;test->test", localhost, database, mysql)



lazy val localhost = (project in General.componentBase / "localhost").enablePlugins(JavaAppPackaging)
  .settings(General.settings("localhost")).dependsOn(commons  % "compile->compile;test->test")

lazy val commons = (project in General.subprojectBase / "commons").enablePlugins(JavaAppPackaging)
  .settings(General.settings("commons"))
  .settings(libraryDependencies ++= Commons.dependencies)

lazy val database = (project in General.componentBase / "database" / "database-common").enablePlugins(JavaAppPackaging)
  .settings(General.settings("database-common")).dependsOn(commons  % "compile->compile;test->test")

lazy val mysql = (project in General.componentBase / "database" / "mysql").enablePlugins(JavaAppPackaging)
  .settings(General.settings("mysql"))
  .dependsOn(commons  % "compile->compile;test->test")
  .dependsOn(database  % "compile->compile;test->test")
  .settings(MySQL.settings)


lazy val all = (project in file("all")).aggregate(ultron ,commons,localhost, database, mysql)
  .enablePlugins(JavaAppPackaging)
  .settings(unidocSettings)
  .settings(site.settings ++ ghpages.settings: _*)
  .settings(
    coverageEnabled := true,
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(ultron),
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "latest/api"),
    gitRemoteRepo := "git@github.com:mig-foxbat/ultron.git"
  )
















