package tech.artemesia.task.database.mysql

import java.io.File

import tech.artemesia.core.AppLogger
import tech.artemesia.task.database.DataLoader
import tech.artemesia.task.settings.LoadSettings

/**
 * Created by chlr on 5/1/16.
 */

trait MySQLDataLoader extends DataLoader {
  self: MysqlDBInterface =>

  override def loadData(tableName: String, loadSettings: LoadSettings, errorFile: File): Long = {
    AppLogger debug "error file is ignored in this mode"
    this.execute(getLoadSQL(loadSettings, tableName))
  }

  def getLoadSQL(loadSettings: LoadSettings, tableName: String) = {
    s"""
        | LOAD DATA LOCAL INFILE '${loadSettings.location.getPath}'
        | INTO TABLE $tableName FIELDS TERMINATED BY '${loadSettings.delimiter}' ${if (loadSettings.quoting) s"OPTIONALLY ENCLOSED BY '${loadSettings.quotechar}'"  else ""}
        | ESCAPED BY '${if (loadSettings.escapechar == '\\') "\\\\" else loadSettings.escapechar }'
        | IGNORE ${loadSettings.skipRows} LINES
     """.stripMargin
  }
}
