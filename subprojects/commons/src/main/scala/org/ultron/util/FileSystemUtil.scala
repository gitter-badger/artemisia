package org.ultron.util

import java.io._
import java.nio.file.Paths

/**
 * Created by chlr on 3/6/16.
 */
object FileSystemUtil {

  def readResource(resource: String) = {
    val resource_stream = this.getClass.getResourceAsStream(resource)
    val buffered = new BufferedReader(new InputStreamReader(resource_stream))
    buffered.lines().toArray.mkString("\n")
  }

  def writeFile(content: String,file: String, append: Boolean = true) {
    val handle = new File(file)
    handle.getParentFile.mkdirs()
    val writer = new BufferedWriter(new FileWriter(handle,append))
    writer.write(content)
    writer.close()
  }

  def fileExists(file: String): Boolean = {
    new File(file).exists()
  }

  def joinPath(path: String*) = {
    path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
  }

  def getCurrentWorkdingDir = {
    Paths.get("").toAbsolutePath
  }

}
