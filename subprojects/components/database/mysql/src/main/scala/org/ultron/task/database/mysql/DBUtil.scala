package org.ultron.task.database.mysql

import java.sql.{DriverManager, ResultSet}

import org.ultron.util.db.ConnectionProfile

/**
 * Created by chlr on 4/13/16.
 */

class DBUtil(connectionProfile: ConnectionProfile) {

  def queryWithConnection(connectionProfile: ConnectionProfile)(sql: => String): ResultSet = {
    Class.forName("com.mysql.jdbc.Driver")
     val connection = DriverManager.getConnection(
      s"""jdbc:mysql://${connectionProfile.hostname}/${connectionProfile.default_database}?
         |user=&${connectionProfile.username}password=${connectionProfile.password}""".stripMargin)
    val stmt = connection.prepareStatement(sql)
    stmt.executeQuery()
  }


  def executeWithConnection(connectionProfile: ConnectionProfile)(sql: String)

}
