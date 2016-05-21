package tech.artemisia.task.localhost

import com.typesafe.config.Config
import tech.artemisia.task.{Task, Component}


/**
 * Created by chlr on 2/21/16.
 */
class LocalHostComponents extends Component {
  override def dispatch(task: String, name: String, config: Config): Task = {
    task match {
      case "ScriptTask" => ScriptTask(name,config)
    }
  }
}
