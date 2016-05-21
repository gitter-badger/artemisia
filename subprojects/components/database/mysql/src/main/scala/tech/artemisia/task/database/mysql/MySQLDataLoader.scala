package tech.artemisia.task.database.mysql

import tech.artemisia.core.AppLogger
import tech.artemisia.task.database.{DBInterface, DataLoader}
import tech.artemisia.task.settings.LoadSettings

/**
 * Created by chlr on 5/1/16.
 */

trait MySQLDataLoader extends DataLoader {
  self: DBInterface =>

  override def loadData(tableName: String, loadSettings: LoadSettings) = {
    AppLogger debug "error file is ignored in this mode"
    this.execute(getLoadSQL(loadSettings, tableName)) -> -1L
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
