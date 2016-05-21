package tech.artemisia.util

import tech.artemisia.TestSpec
/**
 * Created by chlr on 12/11/15.
 */
class FileSystemUtilSpec extends TestSpec  {

  "joinPath" must "Join multiple path strings into valid path string" in {
    var path1 = "/var/tmp"
    var path2 = "artemesia"
    var path3 = ""
    FileSystemUtil.joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "/var/tmp/"
    path2 = "/artemesia"
    FileSystemUtil.joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "/var/tmp"
    path2 = "/artemesia"
    FileSystemUtil.joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
    path1 = "var"
    path2 = "tmp"
    path3 = "artemesia"
    FileSystemUtil.joinPath (path1,path2,path3) must be ("/var/tmp/artemesia")
  }

  "makeURI" must "properly construct URI object" in {
    val path1 = "/var/tmp/dir"
    val path2 = "hdfs://var/tmp/dir2"
    FileSystemUtil.makeURI(path1).toString must be ("file:/var/tmp/dir")
    FileSystemUtil.makeURI(path2).toString must be ("hdfs://var/tmp/dir2")
  }

}
