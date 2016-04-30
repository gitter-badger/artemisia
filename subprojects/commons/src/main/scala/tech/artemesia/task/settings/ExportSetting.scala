package tech.artemesia.task.settings

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.util.HoconConfigUtil
import HoconConfigUtil.Handler

/**
 * Created by chlr on 4/13/16.
 */

/**
 * A case class for storing Export settings
 * @param file Target file to store exported data
 * @param header include header in file
 * @param delimiter delimiter of the file
 * @param quoting enable/disable quoting fields
 * @param quotechar character to be used for quoting if enabled
 * @param escapechar escape character to be used for escaping special characters
 */
case class ExportSetting(file: File, header: Boolean = false, delimiter: Char = ',', quoting: Boolean = false,
                          quotechar: Char = '"', escapechar: Char = '\\')

object ExportSetting {

  val default_config = ConfigFactory parseString
    """
      | {
      |	  header = false
      |	  delimiter = ","
      |	  quoting = no,
      |	  quotechar = "\""
      |   escapechar = "\\"
      |	}
      |
    """.stripMargin

  def apply(inputConfig: Config): ExportSetting = {
    val config = inputConfig withFallback default_config
    ExportSetting(
      file = new File(config.as[String]("file")),
      header = config.as[Boolean]("header"),
      delimiter = config.as[Char]("delimiter"),
      quoting = config.as[Boolean]("quoting"),
      escapechar = config.as[Char]("escapechar"),
      quotechar =  config.as[Char]("quotechar")
    )
  }
}
