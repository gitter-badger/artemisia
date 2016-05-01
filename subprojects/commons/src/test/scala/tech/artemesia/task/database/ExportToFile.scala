package tech.artemesia.task.database

import com.typesafe.config.Config
import tech.artemesia.task.Task
import tech.artemesia.task.settings.{ConnectionProfile, LoadSettings}

/**
 * Created by chlr on 4/30/16.
 */

abstract class ExportToFile(name: String, targetTable: String, loadSettings: LoadSettings, connectionProfile: ConnectionProfile)
    extends Task(name) {

  val dbInterface: DBInterface

  override protected[task] def work(): Config = {
    dbInterface.loadFile(targetTable ,loadSettings)
  }

}
