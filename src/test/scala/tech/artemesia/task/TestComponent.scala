package tech.artemesia.task

import com.typesafe.config.Config

/**
 * Created by chlr on 1/26/16.
 */
class TestComponent extends Component {
  override def dispatch(task: String, name: String, params: Config): Task = {
    task match {
      case "TestAdderTask" =>  TestAdderTask(params)
      case "FailTask" => new TestFailTask()
    }
  }
}
