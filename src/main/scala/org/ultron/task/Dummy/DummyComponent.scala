package org.ultron.task.dummy

import com.typesafe.config.Config
import org.ultron.task.{Component, Task, TaskConfig, UnKnownTaskException}

/**
 * Created by chlr on 1/9/16.
 */
class DummyComponent extends Component {

  override protected def getTaskHandler(task: String)(params: Config, task_config: TaskConfig): Task = {
    task match {
      case "DummyTask" => DummyTask(params,task_config)
      case _ => throw new UnKnownTaskException(s" unknown task $task")
    }
  }

}








