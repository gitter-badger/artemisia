import sbt._

object Commons {

  val dependencies = Seq (
    "com.google.guava" % "guava" % "19.0",
     "net.sf.opencsv" % "opencsv" % "2.3",
     "com.h2database" % "h2" % "1.4.191" % "test"
  )

}