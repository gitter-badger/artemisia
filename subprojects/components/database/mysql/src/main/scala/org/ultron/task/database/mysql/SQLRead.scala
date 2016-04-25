package org.ultron.task.database.mysql

import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.task.database.DBInterface
import org.ultron.task.settings.ConnectionProfile
import org.ultron.util.Util
import org.ultron.util.HoconConfigUtil.Handler

class SQLRead(name: String = Util.getUUID, sql: String, connectionProfile: ConnectionProfile)
  extends org.ultron.task.database.SQLRead(name, sql, connectionProfile) {

  override val dbInterface: DBInterface = new MysqlDBInterface(connectionProfile)

}

object SQLRead {


  /**
   *
   * @param name task name
   * @param config configuration for the task
   * @return ExportToFile object
   */
  def apply(name: String,inputConfig: Config) = {

    val config = inputConfig withFallback default_config
    val connectionProfile = ConnectionProfile.parseConnectionProfile(config)
    val sql =  config.as[String]("sql")
    new SQLRead(name,sql,connectionProfile)
  }
}