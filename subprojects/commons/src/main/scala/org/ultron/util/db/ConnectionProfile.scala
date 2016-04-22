package org.ultron.util.db

import com.typesafe.config.{ConfigRenderOptions, Config}
import org.ultron.core.Keywords
import org.ultron.task.TaskContext
import org.ultron.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 4/13/16.
 */

case class ConnectionProfile(hostname: String, username: String, password: String, default_database: String, port: Int)

object ConnectionProfile {

  def apply(config: Config): ConnectionProfile = {
      println(config.root().render(ConfigRenderOptions.concise()))
       ConnectionProfile(
      hostname = config.as[String](Keywords.Connection.HOSTNAME),
      username = config.as[String](Keywords.Connection.USERNAME),
      password = config.as[String](Keywords.Connection.PASSWORD),
      default_database = config.as[String](Keywords.Connection.DATABASE),
      port = config.as[Int](Keywords.Connection.PORT)
    )
  }

  def apply(connectionName: String): ConnectionProfile = {
    TaskContext.predefinedConnectionProfiles(connectionName)
  }

}