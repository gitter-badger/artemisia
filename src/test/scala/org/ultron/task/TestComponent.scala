package org.ultron.task

import com.typesafe.config.Config

/**
 * Created by chlr on 1/26/16.
 */
class TestComponent extends Component {
  override protected def getTaskHandler(task: String)(params: Config, task_config: TaskConfig): Task = {
    task match {
      case "TestAdderTask" =>  TestAdderTask(params,task_config)
      case "FailTask" => new TestFailTask(task_config)
    }
  }
}
