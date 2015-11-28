package org.ultron.util

import java.io._
import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigFactory}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.LoggerFactory
import org.ultron.config.Keywords
import org.ultron.core.{AppLogger, wire}
import scaldi.Injectable._

/**
 * Created by chlr on 11/29/15.
 */
object Util {

  val logger = LoggerFactory.getLogger(this.getClass)

  def getGlobalConfigFileLocation(default_file: String = Keywords.Config.DEFUALT_GLOBAL_CONFIG_FILE): Option[String] = {
    val os_util = inject[OSUtil]
    val result = os_util.getSystemVariable(Keywords.Config.GLOBAL_FILE_REF_VAR)
    val global_config = if ( result.isEmpty )
        if (Files exists Paths.get(default_file)) Some(default_file) else None
    else result
    global_config
  }

  def readConfigFile(path: String): Config = {
    if(!Files.exists(Paths.get(path))) {
      AppLogger error s"requested config file $path not found"
      throw new FileNotFoundException(s"The Config file $path is missing")
    }
    ConfigFactory.parseFile(new File(path))
  }

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

  def getUUID = {
    java.util.UUID.randomUUID.toString
  }

  def joinPath(path: String*) = {
    path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
  }

  def printStackTrace(ex: Throwable) = {
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    ex.printStackTrace(pw)
    sw.toString
  }

  def currentTime : String = {
      val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
      formatter.print(new DateTime())
  }

}
