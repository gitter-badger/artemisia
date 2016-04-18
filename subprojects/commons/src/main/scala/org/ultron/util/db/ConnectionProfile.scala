package org.ultron.util.db

import com.typesafe.config.Config
import org.ultron.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 4/13/16.
 */

case class ConnectionProfile(hostname: String, username: String, password: String, default_database: String, port: Int)

object ConnectionProfile {

  def apply(config: Config): ConnectionProfile = {
       ConnectionProfile(
      hostname = config.as[String]("host"),
      username = config.as[String]("username"),
      password = config.as[String]("password"),
      default_database = config.as[String]("database"),
      port = config.as[Int]("port")
    )
  }
}