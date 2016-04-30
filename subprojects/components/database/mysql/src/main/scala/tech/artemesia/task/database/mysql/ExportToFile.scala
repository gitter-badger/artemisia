package tech.artemesia.task.database.mysql

import java.io.File

import com.typesafe.config.Config
import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.{SettingNotFoundException, ExportSetting, ConnectionProfile}
import tech.artemesia.util.HoconConfigUtil.Handler
import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.{ExportSetting, ConnectionProfile}

/**
 * Created by chlr on 4/22/16.
 */

class ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting)
  extends tech.artemesia.task.database.ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting) {

  override val dbInterface: DBInterface = new MysqlDBInterface(connectionProfile)

}

object ExportToFile {

  /**
   *
   * @param name task name
   * @param config configuration for the task
   * @return ExportToFile object
   */
  def apply(name: String,inputConfig: Config) = {

    val config = inputConfig withFallback default_config
    val exportSettings = ExportSetting(config.as[Config]("export"))
    val connectionProfile = ConnectionProfile.parseConnectionProfile(config)
    val sql =
      if (config.hasPath("sql")) config.as[String]("sql")
      else if (config.hasPath("sqlfile")) config.asFile("sqlfile")
      else throw new SettingNotFoundException("sql/sqlfile key is missing")
    new ExportToFile(name,sql,connectionProfile,exportSettings)
  }
}


