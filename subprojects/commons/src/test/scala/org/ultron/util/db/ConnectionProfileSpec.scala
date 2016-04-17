package org.ultron.util.db

import com.typesafe.config.ConfigFactory
import org.ultron.TestSpec

/**
 * Created by chlr on 4/16/16.
 */
class ConnectionProfileSpec extends TestSpec {

  "ConnectionProfile" must "correctly construct the object from config" in {
    val config = ConfigFactory parseString
      """
        |	connection = {
        |		host = "database-host"
        |		username = "tango"
        |		password = "bravo"
        |		database = "november"
        |		port = 1000
        |	}
      """.stripMargin

    val connectionProfile = ConnectionProfile(config)
    connectionProfile.hostname must be ("database-host")
    connectionProfile.username must be ("tango")
    connectionProfile.password must be ("bravo")
    connectionProfile.default_database must be ("november")
    connectionProfile.port must be (1000)
  }
}
