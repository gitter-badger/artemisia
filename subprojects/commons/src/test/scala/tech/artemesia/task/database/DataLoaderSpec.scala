package tech.artemesia.task.database

import tech.artemesia.TestSpec
import tech.artemesia.task.settings.LoadSettings
import tech.artemesia.util.FileSystemUtil.FileEnhancer
import tech.artemesia.util.FileSystemUtil.withFile
import tech.artemesia.util.HoconConfigUtil.Handler

/**
 * Created by chlr on 5/13/16.
 */
class DataLoaderSpec extends TestSpec {


  "DataLoader" must "properly load a file" in {
    val tableName = "DataLoaderSpec_1"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    withFile("DataLoaderSpec1") {
        file => {
          val loadSettings = LoadSettings(file.toURI)
          file <<=
            """ |100,tango
                |101,bravo
                |102,whiskey
                |103a,blimey
                |104,victor
            """.stripMargin
          val recordCnt = dbInterface.load(tableName,loadSettings)
          val config = dbInterface.queryOne(s"SELECT COUNT(*) as cnt FROM $tableName")
          config.as[Int]("CNT") must be (4)
          recordCnt must be (4)
      }
    }
  }


//  "DataLoader" must "properly load a file" in {
//    val tableName = "DataLoaderSpec_1"
//    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
//    dbInterface.execute(s"delete from $tableName")
//    withFile("DataLoaderSpec1") {
//      file => {
//        val loadSettings = LoadSettings(file.toURI, delimiter = '\t')
//        file <<=
//          """|100 tango
//             |101 bravo
//             |102 whiskey
//             |104a victor
//          """.stripMargin
//        dbInterface.load(tableName,loadSettings)
//        val config = dbInterface.queryOne(s"SELECT COUNT(*) as cnt FROM $tableName")
//        config.as[Int]("CNT") must be (4)
//      }
//    }
//  }


}
