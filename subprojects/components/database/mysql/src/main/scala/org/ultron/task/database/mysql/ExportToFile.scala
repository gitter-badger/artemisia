package org.ultron.task.database.mysql

import com.typesafe.config.{ConfigFactory, Config}
import org.ultron.task.Task
import org.ultron.util.HoconConfigUtil.Handler
import org.ultron.util.db.{DBUtil, ConnectionProfile, ExportSettings}

/**
 * Created by chlr on 4/13/16.
 */
class ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSettings)
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
      |	export = {
      |	  header = false
      |	  delimiter = ','
      |	  quoting = no,
      |	  quotechar = '"'
      |	}
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
    val exportSettings = ExportSettings(config.as[Config]("export"))
    val connectionProfile = ConnectionProfile(config.as[Config]("connection"))
    val sql =  config.as[String]("sql")
    new ExportToFile(name,sql,connectionProfile,exportSettings)
  }
}


