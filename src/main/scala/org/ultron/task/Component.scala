package org.ultron.task

import com.typesafe.config.Config
import org.ultron.config.{AppContext, Keywords}
import org.ultron.core.AppLogger

/**
 * Created by chlr on 1/7/16.
 */
abstract class Component {

  protected def getTaskHandler(task: String)(params: Config,task_config: TaskConfig): Task

  final def getTask(task: String,name: String,body: Config, app_context: AppContext): Task = {
    val task_config = TaskConfig(name,body,app_context)
    val params = body.getObject(Keywords.Task.PARAMETERS).toConfig
    val result = this.getTaskHandler(task)(params,task_config)
    AppLogger debug s"$task matched with ${result.getClass.getCanonicalName}"
    result
  }

}

class UnKnownTaskException(message: String) extends Exception(message)
