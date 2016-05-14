package tech.artemesia.task.database

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.core.AppLogger
import tech.artemesia.task.Task
import tech.artemesia.task.settings.{ConnectionProfile, LoadSettings}

/**
 * Created by chlr on 4/30/16.
 */

abstract class LoadToTable(name: String, tablename: String, connectionProfile: ConnectionProfile,
                           loadSettings: LoadSettings) extends Task(name) {

  val dbInterface: DBInterface

  override def work(): Config = {
    val recordCnt = dbInterface.load(tablename, loadSettings, this.getFileHandle("error_file.txt"))
    AppLogger info s"$recordCnt rows loaded into table $tablename"
    ConfigFactory parseString s" { rows = $recordCnt }"
  }

}
