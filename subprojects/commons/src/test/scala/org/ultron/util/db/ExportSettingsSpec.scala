package org.ultron.util.db

import com.typesafe.config.ConfigFactory
import org.ultron.TestSpec

/**
 * Created by chlr on 4/16/16.
 */
class ExportSettingsSpec extends TestSpec {

  "ExportSetting" must "properly construct object from Config" in {
      val config = ConfigFactory parseString
        """
          |export = {
          | file = export.dat
          |	header = yes
          |	delimiter = "\t"
          |	quoting = no
          |	escapechar = "\\"
          |	quotechar = "\""
          |}
        """.stripMargin
    val setting = ExportSettings(config)
    setting.escapechar must be ('\\')
    setting.includeHeader must be (true)
    setting.delimiter must be ('\t')
    setting.quotechar must be ('"')
    setting.file.getName must be ("export.dat")
  }
}
