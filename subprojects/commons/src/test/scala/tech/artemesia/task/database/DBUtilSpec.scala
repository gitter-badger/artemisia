package tech.artemesia.task.database

import java.io.File
import tech.artemesia.TestSpec
import tech.artemesia.task.settings.ExportSetting

/**
 * Created by chlr on 4/27/16.
 */
class DBUtilSpec extends TestSpec {

  val table = "db_utilspec"
  val dbInterface = TestDBInterFactory.createDBInterface(table)

  "DBUtil" must "export resultset to file with recordcount" in {
    val file: File = new File(this.getClass.getResource("/exports/DBUtilSpec.txt").getFile)
    val cnt = DBUtil.exportCursorToFile(dbInterface.query(s"select * from $table"), ExportSetting(file = file.toURI, delimiter = '\t'))
    cnt must be (2)
    val content = scala.io.Source.fromFile(file).mkString.split("\n")
    content(0) must be ("1\tfoo")
    content(1) must be ("2\tbar")
  }



}
