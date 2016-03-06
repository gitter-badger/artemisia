package org.ultron.util.db

import java.sql.{Connection, ResultSet}
import com.typesafe.config.{ConfigFactory, Config}
import org.ultron.core.AppLogger
import org.ultron.util.Util


/**
 * Created by chlr on 4/13/16.
 */


/**
 * A standard database interface for common operations such as
 *   - query result
 *   - execute DML statements
 *   - query and parse and store results HoconConfig objects
 *   - close connection
 */
trait DBInterface {

  /**
   *
   * @return connection to the database
   */
  def connection: Connection

  /**
   *
   * @param sql Select query to be executed
   * @return result object
   */
  def query(sql: String): ResultSet = {
    AppLogger info "executing query"
    AppLogger info Util.prettyPrintAsciiTable(sql,"query")
    val stmt = connection.prepareStatement(sql)
    stmt.executeQuery()
  }

  /**
   *
   * @param sql DML query to be executed
   * @return number of records updated/deleted/inserted
   */
  def execute(sql: String): Int = {
    AppLogger info "executing query"
    AppLogger info Util.prettyPrintAsciiTable(sql,"query")
    val stmt = connection.prepareStatement(sql)
    val recordCnt = stmt.executeUpdate()
    stmt.close()
    recordCnt
  }

  /**
   *
   * @param sql Select query to be executed
   * @return Hocon Config object of the first record
   */
  def queryOne(sql: String): Config = {
    AppLogger info "executing query"
    AppLogger info Util.prettyPrintAsciiTable(sql,"query")
    val stmt = connection.prepareStatement(sql)
    val rs = stmt.executeQuery()
    val result = DBInterface.resultSetToConfig(rs)
    try {
      rs.close()
      stmt.close()
    }
    catch {
      case e: Throwable => {
        AppLogger warn e.getMessage
      }
    }
    result
  }

  /**
   * close the database connection
   */
  def terminate(): Unit = {
    connection.close()
  }

}


object DBInterface {

  /**
   *
   * @todo convert java.sql.Time to Duration type in Hocon
   * @param rs input ResultSet
   * @return config object parsed from first row
   */
  def resultSetToConfig(rs: ResultSet) = {
    if(rs.next()) {
      val result = for(i <- 1 to rs.getMetaData.getColumnCount) yield {
        rs.getMetaData.getColumnName(i) -> rs.getObject(i)
      }
      result.foldLeft[Config](ConfigFactory.empty()) {
        (config: Config, data: (String, Object)) => {
          val parsed = data match {
            case (x, y: String) =>  s"$x = $y"
            case (x, y: java.lang.Number) => s"$x = ${y.toString}"
            case (x, y: java.lang.Boolean) => s"$x = ${if (y) "yes" else "no"}"
            case (x, y: java.util.Date) => s"$x = ${y.toString}"
            case _ => throw new UnsupportedOperationException(s"Type ${data._2.getClass.getCanonicalName} is not supported")
          }
          ConfigFactory.parseString(parsed) withFallback config
        }
      }
    }
    else {
      ConfigFactory.empty()
    }
  }
}





