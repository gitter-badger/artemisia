package org.ultron.util

import java.io.File

/**
 * Created by chlr on 12/9/15.
 */
trait OSUtil {

  def joinPath(path: String*) = {
    path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
  }

  def getSystemVariable(variable: String): Option[String]

  def getSystemProperties(variable: String): Option[String]

}


class OSUtilImpl extends OSUtil {

  override def getSystemVariable(variable: String): Option[String] = {
    val result = scala.util.Properties.envOrNone(variable)
    result
  }

  override def getSystemProperties(property: String) = {
    Option(System.getProperty(property))
  }

}
