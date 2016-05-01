package tech.artemesia.task.settings

import java.net.URI
import tech.artemesia.util.HoconConfigUtil.Handler
import com.typesafe.config.{ConfigFactory, Config}

/**
 * Created by chlr on 4/30/16.
 */

/**
 * Load settings definition
 */
case class LoadSettings(location: URI, skipRows: Int = 0, delimiter: Char = ',', quoting: Boolean = false,
                        quotechar: Char = '"', escapechar: Char = '\\') {

}

object LoadSettings {

  val default_config = ConfigFactory parseString
    """
      |
      |{
      |	header =  no
      |	skip-lines = 0
      |	delimiter = ","
      |	quoting = no
      |	quotechar = "\""
      | escapechar = "\\"
      |}
    """.stripMargin

  def apply(inputConfig: Config): LoadSettings = {
    val config = inputConfig withFallback default_config
    LoadSettings (
    location = new URI(config.as[String]("load-path")),
    skipRows = if (config.as[Int]("skip-lines") == 0) if (config.as[Boolean]("header")) 1 else 0 else config.as[Int]("skip-lines"),
    delimiter = config.as[Char]("delimiter"),
    quoting = config.as[Boolean]("quoting"),
    quotechar = config.as[Char]("quotechar"),
    escapechar = config.as[Char]("escapechar")
    )
  }

}
