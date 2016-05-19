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
    val (totalRows, rejectedCnt) = dbInterface.load(tablename, loadSettings)
    AppLogger info s"${totalRows - rejectedCnt} rows loaded into table $tablename"
    AppLogger info s"$rejectedCnt row were rejected"
    wrapAsStats {
      ConfigFactory parseString
        s"""
           |loaded = ${totalRows-rejectedCnt}
           |rejected = $rejectedCnt
         """.stripMargin
    }
  }

}
