
import sbt._

object Dependencies {

  val commonSettings = Seq (
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "ch.qos.logback" % "logback-classic" % "0.9.28",
    "com.iheart" %% "ficus" % "1.2.0",
    "ch.qos.logback" % "logback-classic" % "0.9.28" % "runtime",
    "org.slf4j" % "slf4j-api" % "1.7.6" % "provided",
    "org.slf4j" % "slf4j-nop" % "1.7.6" % "test",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.pegdown" % "pegdown" % "1.0.2" % "test"
  )

  val coreSettings = Seq (
    "com.github.scopt" %% "scopt" % "3.3.0",
    "com.typesafe.akka" %% "akka-actor" % "2.3.11",
    "org.scalaz" %% "scalaz-core" % "7.2.0",
    "org.scaldi" %% "scaldi" % "0.5.6",
    "joda-time" % "joda-time" % "2.0",
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.1" % "test"
  )



}