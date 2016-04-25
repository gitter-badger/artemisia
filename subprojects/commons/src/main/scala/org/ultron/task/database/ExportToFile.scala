package org.ultron.task.database

import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.task.Task
import org.ultron.task.settings.{ConnectionProfile, ExportSetting}

/**
 * Created by chlr on 4/13/16.
 */

/**
  *
  * @param name name of the task instance
  * @param sql query for the export
  * @param connectionProfile Connection Profile settings
  * @param exportSettings Export settings
  */
abstract class ExportToFile(name: String, sql: String, connectionProfile: ConnectionProfile ,exportSettings: ExportSetting)
  extends Task(name: String) {

     val dbInterface: DBInterface

     override protected[task] def setup(): Unit = {}

     /**
      *
      * SQL export to file
      * @return Empty
      */
     override protected[task] def work(): Config = {
       val rs = dbInterface.query(sql)
       DBUtil.exportCursorToFile(rs,exportSettings)
       ConfigFactory.empty()
     }

     override protected[task] def teardown(): Unit = {}

   }




