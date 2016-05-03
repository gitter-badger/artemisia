import com.typesafe.sbt.SbtGit.GitKeys._
import sbt._
import sbtunidoc.Plugin.UnidocKeys._
import Modules._

assemblySettings


coverageEnabled.in(ThisBuild ,Test, test) := true

//addCommandAlias("full-test", "clean;test;coverageReport")


lazy val artemesia = (project in file(".")).enablePlugins(JavaAppPackaging)
  .settings(General.settings("artemesia"))
  .settings(
  libraryDependencies ++= Artemesia.dependencies
).dependsOn(commons % "compile->compile;test->test", localhost, mysql)


lazy val localhost = (project in General.componentBase / "localhost").enablePlugins(JavaAppPackaging)
  .settings(General.settings("localhost")).dependsOn(commons  % "compile->compile;test->test")

lazy val commons = (project in General.subprojectBase / "commons").enablePlugins(JavaAppPackaging)
  .settings(General.settings("commons"))
  .settings(libraryDependencies ++= Commons.dependencies)


lazy val mysql = (project in General.componentBase / "database" / "mysql").enablePlugins(JavaAppPackaging)
  .settings(General.settings("mysql"))
  .dependsOn(commons  % "compile->compile;test->test")
  .settings(MySQL.settings)


lazy val all = (project in file("all")).aggregate(artemesia ,commons,localhost, mysql)
  .enablePlugins(JavaAppPackaging)
  .settings(unidocSettings)
  .settings(site.settings ++ ghpages.settings: _*)
  .settings(
    coverageEnabled := true,
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(artemesia),
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "latest/api"),
    gitRemoteRepo := "git@github.com:mig-foxbat/artemesia.git"
  )
















