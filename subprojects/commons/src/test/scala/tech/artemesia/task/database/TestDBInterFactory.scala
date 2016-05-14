package tech.artemesia.task.database

import java.io.File
import java.sql.{Connection, DriverManager}

import tech.artemesia.task.settings.LoadSettings

/**
 * Created by chlr on 4/27/16.
 */
object TestDBInterFactory {

  def createDBInterface(table: String, mode: Option[String] = None) = {


    val dbInterface: DBInterface = new DBInterface with DataLoader  {
      override def connection: Connection = {
        val modeoption = (mode map { x => s"MODE=$x;" }).getOrElse("")
        Class.forName("org.h2.Driver")
        DriverManager.getConnection(s"jdbc:h2:mem:test;${modeoption}DB_CLOSE_DELAY=-1","","")
      }
    }

    dbInterface.execute(s"CREATE TABLE IF NOT EXISTS $table (col1 int, col2 varchar(10))")
    dbInterface.execute(s"DELETE FROM $table")
    dbInterface.execute(s"INSERT INTO $table VALUES (1, 'foo')")
    dbInterface.execute(s"INSERT INTO $table VALUES (2, 'bar')")

    dbInterface
  }

  trait NopDataLoader extends DataLoader {
    self: DBInterface =>

    override def loadData(tableName: String, loadSetting: LoadSettings, errorFile: File) = {
        println("empty implementation of loader")
      -1
    }
  }
}


