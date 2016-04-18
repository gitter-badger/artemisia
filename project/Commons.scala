import sbt._

object Commons {

  val dependencies = Seq (
    "com.google.guava" % "guava" % "19.0",
     "net.sf.opencsv" % "opencsv" % "2.3",
     "org.apache.commons" % "commons-csv" % "1.0"
  )

}