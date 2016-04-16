package org.ultron.util.db

import java.sql.{Connection, ResultSet}


/**
 * Created by chlr on 4/13/16.
 */


trait DBInterface {

  def connection: Connection

  def query(sql: String): ResultSet = {
    val stmt = connection.prepareStatement(sql)
    stmt.executeQuery()
  }

  def execute(sql: String): Int = {
    val stmt = connection.prepareStatement(sql)
    val recordCnt = stmt.executeUpdate()
    stmt.close()
    recordCnt
  }

  def queryOne(sql: String, column: String): String = {
    val stmt = connection.prepareStatement(sql)
    val rs = stmt.executeQuery()
    rs.getObject(column).toString
  }

  def terminate(): Unit = {
    connection.close()
  }

}





