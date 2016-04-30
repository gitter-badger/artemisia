package tech.artemesia.util

import tech.artemesia.TestSpec
import tech.artemesia.core.env
/**
 * Created by chlr on 12/11/15.
 */
class OSUtilSpec extends TestSpec  {

  val util = env.osUtil

  "joinPath" must "Join multiple path strings into valid path string" in {
    var path1 = "/var/tmp"
    var path2 = "artemesia"
    var path3 = ""
    util joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "/var/tmp/"
    path2 = "/artemesia"
    util joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "/var/tmp"
    path2 = "/artemesia"
    util joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "var"
    path2 = "tmp"
    path3 = "artemesia"
    util joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
  }

}
