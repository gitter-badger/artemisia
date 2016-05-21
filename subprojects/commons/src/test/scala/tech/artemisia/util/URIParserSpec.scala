package tech.artemisia.util

import tech.artemisia.TestSpec

/**
 * Created by chlr on 5/21/16.
 */
class URIParserSpec extends TestSpec {

  "URIParser" must "parse URIs correctly" in {
    val uri = URIParser.parse("hdfs://user/artemisia/file.txt")
    uri.getScheme must be ("hdfs")
  }

  it must "for URIs with schema it must assume its a File URI" in {
    val uri = URIParser.parse("/user/artemisia/file.txt")
    uri.getScheme must be ("file")
  }

}
