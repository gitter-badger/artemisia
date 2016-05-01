package tech.artemesia.task.database

import com.typesafe.config.{ConfigRenderOptions, Config}
import tech.artemesia.core.AppLogger
import tech.artemesia.task.Task
import tech.artemesia.task.settings.ConnectionProfile
import tech.artemesia.util.Util

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
    val result = dbInterface.queryOne(sql)
    AppLogger debug s"query result ${result.root().render(ConfigRenderOptions.concise())}"
    result
  }

  override protected[task] def teardown(): Unit = {}

}
