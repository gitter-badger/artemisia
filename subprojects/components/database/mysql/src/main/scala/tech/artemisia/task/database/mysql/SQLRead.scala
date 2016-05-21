package tech.artemisia.task.database.mysql

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemisia.task.database.DBInterface
import tech.artemisia.task.settings.{SettingNotFoundException, ConnectionProfile}
import tech.artemisia.util.Util
import tech.artemisia.util.HoconConfigUtil.Handler
import tech.artemisia.task.database.DBInterface
import tech.artemisia.util.Util

class SQLRead(name: String = Util.getUUID, sql: String, connectionProfile: ConnectionProfile)
  extends tech.artemisia.task.database.SQLRead(name, sql, connectionProfile) {

  override val dbInterface: DBInterface = DbInterfaceFactory.getInstance(connectionProfile)

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