package tech.artemesia.task.database

import tech.artemesia.TestSpec
import tech.artemesia.task.settings.{LoadSettings, ConnectionProfile}

/**
 * Created by chlr on 5/18/16.
 */
class LoadToTableSpec extends TestSpec {

  "LoadToTable" must "load a file into the given table" in {
    val tableName = "LoadToTableSpec"
//    val connectionProfile = TestDBInterFactory.stubbedConnectionProfile
//    val loadSettings = LoadSettings()
//    LoadToTableSpec.loader(tableName)

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


