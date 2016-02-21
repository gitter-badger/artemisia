package org.ultron.core

import org.slf4j.LoggerFactory
import org.ultron.config.Keywords
import org.ultron.logging.LogSource

/**
 * Created by chlr on 1/10/16.
 */
object AppLogger {

  val logger = LoggerFactory getLogger this.getClass

  def log(source: LogSource, content: String): String = {
    source match {
      case a: LogSource => s"${a.source} -> $content"
      case null => s"${Keywords.APP} -> $content"
    }
  }

  def info(content: String)(implicit source: LogSource = null) = {
    logger info log(source,content)
  }

  def debug(content: String)(implicit source: LogSource = null) = {
    logger info log(source,content)
  }

  def error(content: String)(implicit source: LogSource = null) = {
    logger error log(source,content)
  }

  def warn(content: String)(implicit source: LogSource = null) = {
    logger warn log(source,content)
  }

  def trace(content: String)(implicit source: LogSource = null) = {
    logger trace log(source,content)
  }


}
