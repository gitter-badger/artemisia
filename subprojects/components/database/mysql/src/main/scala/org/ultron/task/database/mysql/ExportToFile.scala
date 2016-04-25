package org.ultron.task.database.mysql

import com.typesafe.config.Config
import org.ultron.task.database.DBInterface
import org.ultron.task.settings.{ExportSetting, ConnectionProfile}
import org.ultron.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 4/22/16.
 */

class ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting)
  extends org.ultron.task.database.ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting) {

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
    val sql =  config.as[String]("sql")
    new ExportToFile(name,sql,connectionProfile,exportSettings)
  }
}


