package tech.artemesia.task.database.mysql

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.{SettingNotFoundException, ConnectionProfile}
import tech.artemesia.util.Util
import tech.artemesia.util.HoconConfigUtil.Handler
import tech.artemesia.task.database.DBInterface
import tech.artemesia.util.Util

class SQLRead(name: String = Util.getUUID, sql: String, connectionProfile: ConnectionProfile)
  extends tech.artemesia.task.database.SQLRead(name, sql, connectionProfile) {

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

    val config = inputConfig withFallback defaultConfig
    val connectionProfile = ConnectionProfile.parseConnectionProfile(config)
    val sql =
      if (config.hasPath("sql")) config.as[String]("sql")
      else if (config.hasPath("sqlfile")) config.asFile("sqlfile")
      else throw new SettingNotFoundException("sql/sqlfile key is missing")
    new SQLRead(name,sql,connectionProfile)
  }
}