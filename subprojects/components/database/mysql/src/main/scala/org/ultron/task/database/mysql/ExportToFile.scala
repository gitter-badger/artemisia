package org.ultron.task.database.mysql

import com.typesafe.config.{ConfigValueType, ConfigFactory, Config}
import org.ultron.task.Task
import org.ultron.util.HoconConfigUtil.Handler
import org.ultron.util.Util
import org.ultron.util.db.{DBUtil, ConnectionProfile, ExportSetting}

/**
 * Created by chlr on 4/13/16.
 */

/**
 *
 * @param name name of the task instance
 * @param sql query for the export
 * @param connectionProfile Connection Profile settings
 * @param exportSettings Export settings
 */
class ExportToFile(name: String = Util.getUUID, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting)
  extends Task(name: String) {


  override protected[task] def setup(): Unit = {}

  /**
   *
   * SQL export to file
   * @return Empty
   */
  override protected[task] def work(): Config = {
    val dbInterface  = env.dbInterface(connectionProfile)
    val rs = dbInterface.query(sql)
    DBUtil.exportCursorToFile(rs,exportSettings)
    ConfigFactory.empty()
  }

  override protected[task] def teardown(): Unit = {}

}

object ExportToFile {

  val default_config = ConfigFactory parseString
    """
      | params: {
      | connection = { port: 3306 }
      |}
      |
    """.stripMargin


  /**
   *
   * @param name task name
   * @param config configuration for the task
   * @return ExportToFile object
   */
  def apply(name: String,inputConfig: Config) = {

    val config = inputConfig withFallback default_config
    val exportSettings = ExportSetting(config.as[Config]("export"))
    val connectionProfile = config.getValue("connection").valueType() match {
      case ConfigValueType.STRING => ConnectionProfile(config.as[String]("connection"))
      case ConfigValueType.OBJECT => ConnectionProfile(config.as[Config]("connection"))
      case x @ _ => throw new IllegalArgumentException(s"Invalid connection node with type $x}")
    }
    val sql =  config.as[String]("sql")
    new ExportToFile(name,sql,connectionProfile,exportSettings)
  }
}


