package org.ultron.util

import org.ultron.TestSpec

/**
 * Created by chlr on 12/11/15.
 */
class OSUtilSpec extends TestSpec  {

  val util = new OSUtil {
    override def getSystemVariable(variable: String): Option[String] = ???
    override def getSystemProperties(variable: String): Option[String] = ???
  }

  "joinPath" must "Join multiple path strings into valid path string" in {
    var path1 = "/var/tmp"
    var path2 = "ultron"
    var path3 = ""
    util joinPath (path1,path2,path3) must be ("/var/tmp/ultron")
    path1 = "/var/tmp/"
    path2 = "/ultron"
    util joinPath (path1,path2,path3) must be ("/var/tmp/ultron")
    path1 = "/var/tmp"
    path2 = "/ultron"
    util joinPath (path1,path2,path3) must be ("/var/tmp/ultron")
    path1 = "var"
    path2 = "tmp"
    path3 = "ultron"
    util joinPath (path1,path2,path3) must be ("/var/tmp/ultron")
  }

}
