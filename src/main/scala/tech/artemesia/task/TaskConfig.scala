package tech.artemesia.task

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.util.HoconConfigUtil
import HoconConfigUtil.Handler
import tech.artemesia.config.AppContext
import tech.artemesia.core.Keywords
import tech.artemesia.core.Keywords.Task

import scala.concurrent.duration.FiniteDuration

/**
 * Created by chlr on 1/9/16.
 */


/*
TaskConfig requires task_name param because the generic Task node requires task_name variable which will be used in logging.
 */
case class TaskConfig(taskName: String = null,retryLimit : Int, cooldown: FiniteDuration, skipExecution: Boolean = false, ignoreFailure: Boolean = false) {

}

object TaskConfig {

  def apply(taskName: String, inputConfig: Config, appContext: AppContext): TaskConfig = {

    val default_config = ConfigFactory parseString {
      s"""
         |${Task.IGNORE_ERROR} = ${appContext.dagSetting.ignore_conditions}
         |${Keywords.Task.ATTEMPT} = ${appContext.dagSetting.attempts}
         |${Keywords.Task.SKIP_EXECUTION} = false
         |${Keywords.Task.COOLDOWN} = ${appContext.dagSetting.cooldown}
         |__context__ = {}
    """.stripMargin
    }


    val config = inputConfig withFallback default_config
    TaskConfig(taskName,config.as[Int](Keywords.Task.ATTEMPT),
      config.as[FiniteDuration](Keywords.Task.COOLDOWN),
      config.as[Boolean](Keywords.Task.SKIP_EXECUTION),
      config.as[Boolean](Keywords.Task.IGNORE_ERROR))
  }

}