package tech.artemisia.task.database.mysql

import com.typesafe.config.{ConfigFactory, Config}
import tech.artemisia.task.{Component, Task}

/**
 * Created by chlr on 4/8/16.
 */

class MySQLComponent extends Component {

  val defaultConfig = ConfigFactory parseString
    """
      | params: {
      | dsn = { port: 3306 }
      |}
      |
    """.stripMargin


  override def dispatch(task: String, name: String, config: Config): Task = {
    task match {
      case "ExportToFile" => ExportToFile(name, config withFallback defaultConfig)
      case "SQLRead" => SQLRead(name, config withFallback defaultConfig)
      case "LoadToTable" => LoadToTable(name, config withFallback defaultConfig)
      case "SQLExecute" => SQLExecute(name, config withFallback defaultConfig)
    }
  }
}


