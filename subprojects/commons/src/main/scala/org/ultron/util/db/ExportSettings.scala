package org.ultron.util.db

import java.io.File
import org.ultron.util.HoconConfigUtil.Handler
import com.typesafe.config.Config

/**
 * Created by chlr on 4/13/16.
 */

case class ExportSettings(file: File, includeHeader: Boolean, separator: Char, quoting: Boolean, quotechar: Char,
                          escapechar: Char)

object ExportSettings {

  def apply(config: Config): ExportSettings = {
    ExportSettings(
      file = new File(config.as[String]("file")),
      includeHeader = config.as[Boolean]("export.header"),
      separator = config.as[Char]("export.delimiter"),
      quoting = config.as[Boolean]("export.quoting"),
      escapechar = config.as[Char]("export.escapechar"),
      quotechar =  config.as[Char]("export.quotechar")
    )
  }
}
