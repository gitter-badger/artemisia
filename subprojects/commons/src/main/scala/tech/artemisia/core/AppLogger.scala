package tech.artemisia.core

import org.slf4j.LoggerFactory

/**
 * Created by chlr on 1/10/16.
 */


/**
 *  this object is used to format and log messages generated in the application suite.
 */
object AppLogger {

  val logger = LoggerFactory getLogger this.getClass

  /**
   * log message at info level
   *
   * @param content message to log
   */
  def info(content: String) = {
    logger info content
  }

  /**
   * log message at debug level
   *
   * @param content message to log
   */
  def debug(content: String) = {
    logger info content
  }

  /**
   * log message at error level
   *
   * @param content message to log
   */
  def error(content: String) = {
    logger error content
  }

  /**
   * log message at warn level
   *
   * @param content message to log
   */
  def warn(content: String) = {
    logger warn content
  }

  /**
   * log message at trace level
   *
   * @param content message to log
   */
  def trace(content: String) = {
    logger trace content
  }


}
