package tech.artemesia.task.database

import tech.artemesia.TestSpec
import tech.artemesia.task.settings.LoadSettings
import tech.artemesia.util.FileSystemUtil.FileEnhancer
import tech.artemesia.util.FileSystemUtil.withFile

/**
 * Created by chlr on 5/13/16.
 */
class DataLoaderSpec extends TestSpec {


  "DataLoader" must "properly load a file" in {
    val tableName = "DataLoaderSpec"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    withFile("DataLoaderSpec1") {
        file => {
          val loadSettings = LoadSettings(file.toURI)
          file.write("1,foo")
          file.write("2,bar")
          file.write("3,baz")
          file.flush()
          dbInterface.load(tableName,loadSettings,)
      }
    }
  }

}
