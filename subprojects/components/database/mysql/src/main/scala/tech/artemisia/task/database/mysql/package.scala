package tech.artemisia.task.database

import com.typesafe.config.ConfigFactory

/**
 * Created by chlr on 4/22/16.
 */
package object mysql {

  val defaultConfig = ConfigFactory parseString
    """
      | params: {
      | dsn = { port: 3306 }
      |}
      |
    """.stripMargin

}
