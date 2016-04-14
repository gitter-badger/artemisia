package org.ultron.util.db

/**
 * Created by chlr on 4/13/16.
 */

case class ConnectionProfile(dsn: String, hostname: String, username: String, password: String, default_database: String, port: Int)
