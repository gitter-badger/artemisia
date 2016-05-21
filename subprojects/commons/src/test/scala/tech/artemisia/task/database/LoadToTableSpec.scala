package tech.artemisia.task.database

import tech.artemisia.TestSpec
import tech.artemisia.task.settings.{ConnectionProfile, LoadSettings}
import tech.artemisia.util.FileSystemUtil._
import tech.artemisia.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 5/18/16.
 */
class LoadToTableSpec extends TestSpec {

  "LoadToTable" must "load a file into the given table" in {
    val tableName = "LoadToTableSpec"
    withTempFile(fileName = s"${tableName}_1") {
      file => {
        file <<=
          """|102\u0001magneto
             |103\u0001xavier
             |104\u0001wolverine
             |105\u0001mystique
             |106\u0001quicksilver""".stripMargin
        val loadSettings = LoadSettings(file.toURI, delimiter = '\u0001')
        val loader = LoadToTableSpec.loader("LoadToTableSpec1",tableName, TestDBInterFactory.stubbedConnectionProfile,loadSettings)
        val config = loader.execute()
        config.as[Int]("test_task.__stats__.loaded")must be (4)
        config.as[Int]("test_task.__stats__.rejected")must be (1
        )
      }
    }
  }
}

object LoadToTableSpec {

  def loader(name: String, tableName: String, connectionProfile: ConnectionProfile, loadSettings: LoadSettings) =

    new LoadToTable("test_task",tableName, connectionProfile, loadSettings) {
    override val dbInterface: DBInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    override protected[task] def setup(): Unit = {}
    override protected[task] def teardown(): Unit = {}
  }

}


