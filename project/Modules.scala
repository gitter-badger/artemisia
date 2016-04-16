import sbt.Keys._
import sbt._

object Modules {

  object MySQL {
    val settings = Seq(
      libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"
    )
  }

}