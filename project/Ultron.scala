import sbt._

object  Ultron {

  val dependencies = Seq (
    "com.github.scopt" %% "scopt" % "3.3.0",
    "com.typesafe.akka" %% "akka-actor" % "2.3.14",
    "org.scalaz" %% "scalaz-core" % "7.2.0",
    "com.typesafe.akka" %% "akka-testkit" % "2.3.14" % "test"
  )

}