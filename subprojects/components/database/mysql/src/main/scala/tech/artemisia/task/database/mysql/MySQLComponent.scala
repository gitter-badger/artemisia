package tech.artemisia.task.database.mysql

import com.typesafe.config.Config
import tech.artemisia.task.{Task, Component}

/**
 * Created by chlr on 4/8/16.
 */

class MySQLComponent extends Component {

  override def dispatch(task: String, name: String, config: Config): Task = {
    task match {
      case "ExportToFile" => ExportToFile(name, config)
      case "SQLRead" => SQLRead(name, config)
      case "LoadToTable" => LoadToTable(name, config)
    }
  }
}


