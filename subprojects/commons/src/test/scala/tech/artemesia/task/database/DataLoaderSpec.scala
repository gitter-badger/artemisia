package tech.artemesia.task.database

import java.io.File

import tech.artemesia.TestSpec
import tech.artemesia.task.settings.LoadSettings
import tech.artemesia.util.FileSystemUtil.FileEnhancer
import tech.artemesia.util.FileSystemUtil.withFile
import tech.artemesia.util.HoconConfigUtil.Handler

import scala.io.Source

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
                |104,victor """.stripMargin
          val (recordCnt, rejectedCnt) = dbInterface.load(tableName,loadSettings)
          val config = dbInterface.queryOne(s"SELECT COUNT(*) as cnt FROM $tableName")
          config.as[Int]("CNT") must be (4)
          recordCnt must be (5)
          rejectedCnt must be (1)
      }
    }
  }


  it must "presists invalid records in reject file" in {
    val tableName = "DataLoaderSpec_2"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    val errorFile = File.createTempFile("DataLoaderSpec_2","err")
    withFile("DataLoaderSpec2") {
      file => {
        val loadSettings = LoadSettings(file.toURI, delimiter = ',', rejectFile = Some(errorFile.toPath.toString))
        file <<=
          """|100,tango
             |101,bravo
             |102z,whiskey
             |104,victor""".stripMargin
        dbInterface.load(tableName,loadSettings)
        val config = dbInterface.queryOne(s"SELECT COUNT(*) as cnt FROM $tableName")
        config.as[Int]("CNT") must be (3)
        Source.fromFile(errorFile).getLines().mkString("\n") must be ("102z\u0001whiskey")

      }
    }
  }


}
