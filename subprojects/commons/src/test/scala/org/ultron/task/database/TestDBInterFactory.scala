package org.ultron.task.database

import java.sql.{DriverManager, Connection}

/**
 * Created by chlr on 4/27/16.
 */
object TestDBInterFactory {

  def createDBInterface(table: String) = {

    val dbInterface: DBInterface = new DBInterface {
      override def connection: Connection = {
        Class.forName("org.h2.Driver")
        DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1","","")
      }
    }

    dbInterface.execute(s"CREATE TABLE IF NOT EXISTS $table (col1 int, col2 varchar(10))")
    dbInterface.execute(s"DELETE FROM $table")
    dbInterface.execute(s"INSERT INTO $table VALUES (1, 'foo')")
    dbInterface.execute(s"INSERT INTO $table VALUES (2, 'bar')")

    dbInterface
  }

}
