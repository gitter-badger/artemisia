package tech.artemesia.task.database.mysql

import com.typesafe.config.Config
import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.{ConnectionProfile, LoadSettings}
import tech.artemesia.util.Util
import tech.artemesia.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 4/30/16.
 */
class LoadToTable(name: String = Util.getUUID, tablename: String, connectionProfile: ConnectionProfile, loadSettings: LoadSettings)
  extends tech.artemesia.task.database.LoadToTable(name, tablename, connectionProfile, loadSettings) {

  override val dbInterface: DBInterface = DbInterfaceFactory.getInstance(connectionProfile, loadSettings.mode)

  /**
   * No operations are done in this phase
   */
  override protected[task] def setup(): Unit = {}

  /**
   * No operations are done in this phase
   */
  override protected[task] def teardown(): Unit = {}

}

object LoadToTable {

  def apply(name: String, inputConfig: Config): LoadToTable = {
    val config = inputConfig withFallback defaultConfig
    val connectionProfile = ConnectionProfile.parseConnectionProfile(config)
    val destinationTable = config.as[String]("destination-table")
    val loadSettings = LoadSettings(config.as[Config]("load-setting"))
    new LoadToTable(name, destinationTable, connectionProfile, loadSettings)
  }
}
