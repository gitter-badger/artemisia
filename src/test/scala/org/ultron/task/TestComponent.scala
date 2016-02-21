package org.ultron.task

import com.typesafe.config.Config

/**
 * Created by chlr on 1/26/16.
 */
class TestComponent extends ComponentDispatcher {
  override protected def getTaskHandler(task: String)(params: Config, task_config: TaskConfig): TaskHandler = {
    task match {
      case "TestAdderTask" =>  TestAdderTask(params,task_config)
      case "FailTask" => new TestFailTask(task_config)
    }
  }
}
