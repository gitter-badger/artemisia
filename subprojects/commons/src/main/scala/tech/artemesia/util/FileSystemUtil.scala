package tech.artemesia.util

import java.io._
import java.io.BufferedReader
import java.net.URI

/**
 * Created by chlr on 3/6/16.
 */


/**
 * An aggregation of Helper methods related to FileSystem based operations
 *
 */
object FileSystemUtil {

  /**
   * read a text-file using the classloader
   *
   * @param resource name of the resource to be read.
   * @return String content of the resource
   */
  def readResource(resource: String) = {
    val resource_stream = this.getClass.getResourceAsStream(resource)
    val buffered = new BufferedReader(new InputStreamReader(resource_stream))
    buffered.lines().toArray.mkString("\n")
  }

  /**
   * write the given content to text-file
   *
   * @param content content to be written
   * @param file path of the file
   * @param append set true to append content to the file.
   */
  def writeFile(content: String,file: File, append: Boolean = true) {
    file.getParentFile.mkdirs()
    val writer = new BufferedWriter(new FileWriter(file,append))
    writer.write(content)
    writer.close()
  }


  /**
   * returns a composed path joining input paths
   *
   * {{{
     *   joinPath("/home","/artemesia/","mydirectory")
   * }}}
   *
   * would return /home/artemesia/mydirectory
   *
   * @param path varags of paths to be joined
   * @return new path
   */
  def joinPath(path: String*) = {
    path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
  }

  /**
   * build URI object from the given path. set file as the default scheme
   * @param path path to be converted
   * @return java.net.URI object
   */
  def makeURI(path: String) = {
    if (URI.create(path).getScheme == null)
       new File(path).toURI
    else
      new URI(path)
  }
}
