package tech.artemesia.task.database

import tech.artemesia.TestSpec
import tech.artemesia.TestSpecUtil.withFile

/**
 * Created by chlr on 5/13/16.
 */
class DataLoaderSpec extends TestSpec {


  "DataLoader" must "properly load a file" in {
    val tableName = "DataLoaderSpec"
    val dbInterface = TestDBInterFactory.createDBInterface(tableName)
    dbInterface.execute(s"delete from $tableName")
    withFile("DataLoaderSpec1") {
        file => {
          
      }
    }
  }

}
