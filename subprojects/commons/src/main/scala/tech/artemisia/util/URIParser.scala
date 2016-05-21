package tech.artemisia.util

import java.io.File
import java.net.URI

/**
 * Created by chlr on 5/21/16.
 */

/**
 * a URI parse that takes a string argument and creates an URI.
 * if a schema is not defined in the URI it assumes it is a File URI.
 */
object URIParser {

  /**
   * parse string to URI
   * @param uri input string to be parsed
   * @return URI object
   */
  def parse(uri: String) = {
    val original = new URI(uri)
    if (original.getScheme != null) {
      original
    }
    else {
      val file = new File(uri)
      file.toURI
    }
  }

}
