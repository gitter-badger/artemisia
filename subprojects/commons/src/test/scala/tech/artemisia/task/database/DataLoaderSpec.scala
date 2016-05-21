package tech.artemisia.task.database

import java.io.File

import tech.artemisia.TestSpec
import tech.artemisia.task.settings.LoadSettings
import tech.artemisia.util.FileSystemUtil.{FileEnhancer, withTempFile}
import tech.artemisia.util.HoconConfigUtil.Handler
import scala.io.Source


/**
 * Created by chlr on 5/13/16.
 */
class DataLoaderSpec extends TestSpec {


  "DataLoader" must "properly load a file" in {
    val tableName = "DataLoaderSpec_1"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    withTempFile(fileName = "DataLoaderSpec1") {
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
    withTempFile(fileName = "DataLoaderSpec2") {
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


  it must "fail when rejection percentage is greater than allowed limit" in {
    val tableName = "DataLoaderSpec_3"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    val errorFile = File.createTempFile("DataLoaderSpec_3","err")
    withTempFile(fileName = "DataLoaderSpec3") {
      file => {
        val loadSettings = LoadSettings(file.toURI, delimiter = ',', rejectFile = Some(errorFile.toPath.toString)
          ,errorTolerance = 50)
        file <<=
          """|100,tango
            |101,bravo
            |102z,whiskey
            |103c,victor
            |104d,november""".stripMargin
        val ex = intercept[AssertionError]{
          dbInterface.load(tableName,loadSettings)
        }
        ex.getMessage must be ("assertion failed: Load Error % 60.00 greater than defined limit: 50.0")
      }
    }
  }


  it must "handle blank string as null" in {
    val tableName = "DataLoaderSpec_4"
    val dbInterface = TestDBInterFactory.withDefaultDataLoader(tableName)
    dbInterface.execute(s"delete from $tableName")
    withTempFile(fileName = tableName) {
      file => {
        val loadSettings = LoadSettings(file.toURI, delimiter = ',')
        file <<=
          """|100,tango
             |101,bravo
             |102,
             |,victor""".stripMargin
          dbInterface.load(tableName,loadSettings)
        var result = dbInterface.queryOne(s"SELECT col1 FROM $tableName WHERE col2 IS NULL")
        result.as[Int]("COL1") must be (102)
        result = dbInterface.queryOne(s"SELECT col2 FROM $tableName WHERE col1 IS NULL")
        result.as[String]("COL2") must be ("victor")
      }
    }
  }


}
