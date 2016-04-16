package org.ultron.task.database.mysql

import java.sql.{DriverManager, Connection}

import org.ultron.util.db.{ConnectionProfile, DBInterface}

/**
 * Created by chlr on 4/13/16.
 */

class MysqlDBInterface(connectionProfile: ConnectionProfile) extends DBInterface {

  override def connection: Connection = {
      DriverManager.getConnection(s"jdbc:mysql://${connectionProfile.hostname}/${connectionProfile.default_database}?" +
      s"user=${connectionProfile.username}&password=${connectionProfile.password}")
  }

}
