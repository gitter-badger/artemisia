package tech.artemisia.task.database

import com.typesafe.config.{ConfigFactory, Config}
import tech.artemisia.core.AppLogger
import tech.artemisia.task.Task
import tech.artemisia.task.settings.ConnectionProfile

/**
 * Created by chlr on 5/21/16.
 */

/**
 * An abstract task to execute a query
 * @param name a name for this task
 * @param sql query to be executed
 * @param connectionProfile connection detail for the database
 */
abstract class SQLExecute(name: String, sql: String, connectionProfile: ConnectionProfile) extends Task(name) {

  val dbInterface: DBInterface

  override protected[task] def setup(): Unit

  /**
   * The query is executed in this phase.
   * returns number of rows updated as a status node in config object
   * @return any output of the work phase be encoded as a HOCON Config object.
   */
  override protected[task] def work(): Config = {
    val updatedRows = dbInterface.execute(sql)
    AppLogger debug s"$updatedRows rows updated"
    wrapAsStats {
      ConfigFactory parseString
        s"""
          | updated = $updatedRows
        """.stripMargin
    }
  }

  override protected[task] def teardown(): Unit
}


