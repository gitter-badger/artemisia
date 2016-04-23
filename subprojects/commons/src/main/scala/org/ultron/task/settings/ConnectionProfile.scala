package org.ultron.task.settings

import com.typesafe.config.{Config, ConfigRenderOptions, ConfigValueType}
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

  /**
   *
   * @param config
   * @return
   */
  def parseConnectionProfile(config: Config) = {
    config.getValue("dsn").valueType() match {
      case ConfigValueType.STRING => ConnectionProfile(config.as[String]("dsn"))
      case ConfigValueType.OBJECT => ConnectionProfile(config.as[Config]("dsn"))
      case x @ _ => throw new IllegalArgumentException(s"Invalid connection node with type $x}")
    }
  }

}