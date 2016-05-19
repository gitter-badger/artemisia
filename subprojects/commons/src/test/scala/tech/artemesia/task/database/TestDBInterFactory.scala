package tech.artemesia.task.database

import java.sql.{Connection, DriverManager}

import tech.artemesia.task.settings.ConnectionProfile

/**
 * Created by chlr on 4/27/16.
 */
object TestDBInterFactory {
  
  
  def withDefaultDataLoader(table: String, mode: Option[String] = None) = {
    val dbInterface: DBInterface = new DBInterface with DataLoader  {
      override def connection: Connection = {
        val modeoption = (mode map { x => s"MODE=$x;" }).getOrElse("")
        Class.forName("org.h2.Driver")
        DriverManager.getConnection(s"jdbc:h2:mem:test;${modeoption}DB_CLOSE_DELAY=-1","","")
      }
    }
    processDbInterface(dbInterface, table)
    dbInterface
  }
  
  
  private def processDbInterface(dbInterface: DBInterface, table: String) = {
    dbInterface.execute(s"CREATE TABLE IF NOT EXISTS $table (col1 int, col2 varchar(10))")
    dbInterface.execute(s"DELETE FROM $table")
    dbInterface.execute(s"INSERT INTO $table VALUES (1, 'foo')")
    dbInterface.execute(s"INSERT INTO $table VALUES (2, 'bar')")
  }

  /**
   * This is stubbed ConnectionProfile object primarily to be used along with H2 database which doesn't require a connectionProfile object to work with.
   * @return ConnectionProfile object
   */
  val stubbedConnectionProfile = ConnectionProfile("","","","",-1)



}


