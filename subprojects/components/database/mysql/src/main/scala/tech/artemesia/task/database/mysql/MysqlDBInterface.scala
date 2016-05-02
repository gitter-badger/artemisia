package tech.artemesia.task.database.mysql

import java.sql.{Connection, DriverManager}

import tech.artemesia.task.database.DBInterface
import tech.artemesia.task.settings.ConnectionProfile


/**
 * Created by chlr on 4/13/16.
 */

class MysqlDBInterface(connectionProfile: ConnectionProfile) extends DBInterface with MySQLDataLoader {

  override def connection: Connection = {
      DriverManager.getConnection(s"jdbc:mysql://${connectionProfile.hostname}/${connectionProfile.default_database}?" +
      s"user=${connectionProfile.username}&password=${connectionProfile.password}")
  }

}
