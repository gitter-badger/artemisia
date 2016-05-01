package tech.artemesia.task.database.mysql

import java.sql.{Connection, DriverManager}
import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.{ConnectionProfile, LoadSettings}


/**
 * Created by chlr on 4/13/16.
 */

class MysqlDBInterface(connectionProfile: ConnectionProfile) extends DBInterface {

  override def connection: Connection = {
      DriverManager.getConnection(s"jdbc:mysql://${connectionProfile.hostname}/${connectionProfile.default_database}?" +
      s"user=${connectionProfile.username}&password=${connectionProfile.password}")
  }

  override def loadFile(tableName: String, loadSettings: LoadSettings): Int = {
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
