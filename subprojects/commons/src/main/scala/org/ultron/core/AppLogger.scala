package org.ultron.core

import org.slf4j.LoggerFactory

/**
 * Created by chlr on 1/10/16.
 */


/**
 *  this object is used to Ultron to format and log messages generated in the application suite.
 */
object AppLogger {

  val logger = LoggerFactory getLogger this.getClass

  private def log(source: LogSource, content: String): String = {
    source match {
      case a: LogSource => s"${a.source} -> $content"
      case null => s"${Keywords.APP} -> $content"
    }
  }

  /**
   * log message at info level
   *
   * @param content message to log
   * @param source source of the message
   */
  def info(content: String)(implicit source: LogSource = null) = {
    logger info log(source,content)
  }

  /**
   * log message at debug level
   *
   * @param content message to log
   * @param source source of the message
   */
  def debug(content: String)(implicit source: LogSource = null) = {
    logger info log(source,content)
  }

  /**
   * log message at error level
   *
   * @param content message to log
   * @param source source of the message
   */
  def error(content: String)(implicit source: LogSource = null) = {
    logger error log(source,content)
  }

  /**
   * log message at warn level
   *
   * @param content message to log
   * @param source source of the message
   */
  def warn(content: String)(implicit source: LogSource = null) = {
    logger warn log(source,content)
  }

  /**
   * log message at trace level
   *
   * @param content message to log
   * @param source source of the message
   */
  def trace(content: String)(implicit source: LogSource = null) = {
    logger trace log(source,content)
  }


}
