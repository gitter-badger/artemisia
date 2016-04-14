package org.ultron.task.database.mysql

import com.typesafe.config.Config
import org.ultron.task.Task
import org.ultron.util.db.{ConnectionProfile, ExportSettings}

/**
 * Created by chlr on 4/13/16.
 */
class SQLExport(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSettings)
  extends Task(name: String) {

  /**
   * override this to implement the setup phase
   *
   * This method should have setup phase of the task, which may include actions like
   * - creating database connections
   * - generating relevant files
   */
  override protected[task] def setup(): Unit = {

  }

  /**
   * override this to implemet the work phase
   *
   * this is where the actual work of the task is done, such as
   * - executing query
   * - launching subprocess
   *
   * @return any output of the work phase be encoded as a HOCON Config object.
   */
  override protected[task] def work(): Config = ???

  /**
   * override this to implement the teardown phase
   *
   * this is where you deallocate any resource you have acquired in setup phase.
   */
  override protected[task] def teardown(): Unit = ???

}


