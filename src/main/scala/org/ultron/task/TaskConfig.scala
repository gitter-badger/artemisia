package org.ultron.task

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.config.AppContext
import org.ultron.core.Keywords

import scala.concurrent.duration.FiniteDuration

/**
 * Created by chlr on 1/9/16.
 */


/*
TaskConfig requires task_name param because the generic Task node requires task_name variable which will be used in logging.
 */
case class TaskConfig(task_name: String = null,retry_limit : Int, cooldown: FiniteDuration, skip_execution: Boolean = false, ignore_failure: Boolean = false) {

}

object TaskConfig {

  def apply(task_name: String,input_config: Config, app_context: AppContext): TaskConfig = {

    val default_config = ConfigFactory parseString {
      s"""
         |${Keywords.Task.IGNORE_ERROR} = ${app_context.dag_setting.ignore_conditions}
         |${Keywords.Task.ATTEMPT} = ${app_context.dag_setting.attempts}
         |${Keywords.Task.SKIP_EXECUTION} = false
         |${Keywords.Task.COOLDOWN} = ${app_context.dag_setting.cooldown}
         |__context__ = {}
    """.stripMargin
    }


    val config = input_config withFallback default_config
    TaskConfig(task_name,config.as[Int](Keywords.Task.ATTEMPT),
      config.as[FiniteDuration](Keywords.Task.COOLDOWN),
      config.as[Boolean](Keywords.Task.SKIP_EXECUTION),
      config.as[Boolean](Keywords.Task.IGNORE_ERROR))
  }
}
