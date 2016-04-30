package tech.artemesia.task.database

import com.typesafe.config.ConfigFactory

/**
 * Created by chlr on 4/22/16.
 */
package object mysql {

  val default_config = ConfigFactory parseString
    """
      | params: {
      | connection = { port: 3306 }
      |}
      |
    """.stripMargin

}
