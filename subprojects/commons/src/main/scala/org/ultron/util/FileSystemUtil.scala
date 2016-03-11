package org.ultron.util

import java.io._

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
  def writeFile(content: String,file: String, append: Boolean = true) {
    val handle = new File(file)
    handle.getParentFile.mkdirs()
    val writer = new BufferedWriter(new FileWriter(handle,append))
    writer.write(content)
    writer.close()
  }

  /**
   * check if a file exists or not
   *
   * @param file file-path
   * @return true if file exists false if not
   */

  def fileExists(file: String): Boolean = {
    new File(file).exists()
  }

  /**
   * returns a composed path joining input paths
   *
   * {{{
   *   joinPath("/home","/ultron/","mydirectory")
   * }}}
   *
   * would return /home/ultron/mydirectory
   *
   * @param path varags of paths to be joined
   * @return new path
   */
  def joinPath(path: String*) = {
    path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
  }

}
