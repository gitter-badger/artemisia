package org.ultron.task.database

import com.typesafe.config.Config
import org.ultron.task.Task
import org.ultron.task.settings.ConnectionProfile
import org.ultron.util.Util

/**
 * Created by chlr on 4/22/16.
 */

/**
 *
 * @param name name of the task
 * @param sql query to be executed
 * @param connectionProfile connection profile to use
 */
abstract class SQLRead(name: String = Util.getUUID, sql: String, connectionProfile: ConnectionProfile)
  extends Task(name) {

  val dbInterface: DBInterface

  override protected[task] def setup(): Unit = {}

  /**
   * execute query and parse as config file.
   * considers only the first row of the query
   * @return config file object
   */
  override protected[task] def work(): Config = {
    dbInterface.queryOne(sql)
  }

  override protected[task] def teardown(): Unit = {}

}
