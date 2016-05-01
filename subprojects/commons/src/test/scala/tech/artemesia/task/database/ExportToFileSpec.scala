package tech.artemesia.task.database

import java.io.File
import tech.artemesia.TestSpec
import tech.artemesia.task.settings.{ConnectionProfile, ExportSetting}

/**
 * Created by chlr on 4/28/16.
 */
class ExportToFileSpec extends TestSpec {

  val table = "export_to_file"
  val testDbInterface = TestDBInterFactory.createDBInterface(table)
  val connectionProfile = ConnectionProfile("","","","default", 1000)
  val file = new File(this.getClass.getResource("/exports/ExportToFile.txt").getFile)
  val exportSettings = ExportSetting(file.toURI, delimiter = 0x1)

  "ExportToFile" must "export query result to file" in {
    val exportToFile = new ExportToFile(name = "ExportToFileTest",
    sql = s"select * from $table",
    connectionProfile,
    exportSettings
    ) {
      override val dbInterface: DBInterface = testDbInterface
    }
    exportToFile.execute
    scala.io.Source.fromFile(file).getLines().toList(1) must be ("2\u0001bar")
  }


}
