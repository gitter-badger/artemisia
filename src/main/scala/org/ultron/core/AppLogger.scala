package org.ultron.core

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.slf4j.LoggerFactory
import org.ultron.config.{AppContext, Keywords}
import org.ultron.task.Task

/**
 * Created by chlr on 1/10/16.
 */
object AppLogger {
  
  val logger = LoggerFactory getLogger this.getClass
  
  def log(source: Any, content: String): String = {
      source match {
        case a: Task => s"${a.task_metadata.task_name} -> $content"
        case _ => s"${Keywords.APP} -> $content"
      }
  }
  
  def info(content: String)(implicit source: Task = null) = {
    logger info log(source,content)
  }

  def debug(content: String)(implicit source: Task = null) = {
    logger info log(source,content)
  }

  def error(content: String)(implicit source: Task = null) = {
    logger error log(source,content)
  }

  def warn(content: String)(implicit source: Task = null) = {
    logger warn log(source,content)
  }

  def trace(content: String)(implicit source: Task = null) = {
    logger trace log(source,content)
  }

  def initializeLogging(app_context: AppContext): Unit = {
    val context = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    val jc = new JoranConfigurator
    jc.setContext(context)
    context.reset()
    context.putProperty("log.console.level", app_context.logging.console_trace_level)
    context.putProperty("log.file.level", app_context.logging.file_trace_level)
    context.putProperty("env.working_dir", app_context.core_setting.working_dir)
    context.putProperty("workflow_id", app_context.run_id)
    jc.doConfigure(this.getClass.getResourceAsStream("/logback_config.xml"))
  }

}
