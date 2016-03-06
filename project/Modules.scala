import sbt._
import sbt.Keys._


object Modules {

  object MySQL {
    val settings = Seq(libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6")

  }

}