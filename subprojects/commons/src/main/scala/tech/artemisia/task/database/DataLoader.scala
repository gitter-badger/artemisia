package tech.artemisia.task.database

import java.io.File
import java.math
import java.sql.{SQLException, Types}

import tech.artemisia.core.AppLogger
import tech.artemisia.inventory.io.{CSVFileReader, CSVFileWriter, NullFileWriter}
import tech.artemisia.task.settings.{ExportSetting, LoadSettings}

/**
 * Created by chlr on 5/1/16.
 */

/**
 * A mixin trait 
 */
trait DataLoader {

  self: DBInterface =>


  /**
   * A generic function that loads a file to table by iterating each row of the file
   * and running INSERT INTO TABLE query
   *
   * @param tableName target table to load
   * @param loadSettings load settings
   * @return number of records inserted
   */
  def loadData(tableName: String, loadSettings: LoadSettings) = {

    assert(loadSettings.location.getScheme == "file", s"schema ${loadSettings.location.getScheme} is not supported. file:// is the only supported schema")

    val csvReader = new CSVFileReader(loadSettings)
    val errorWriter = loadSettings.rejectFile.map( x => new CSVFileWriter(ExportSetting(new File(x).toURI,false,'\u0001',false)) ).getOrElse(NullFileWriter)
    var rejectedRecordCounter =  0L
    val parsedTableName = DBUtil.parseTableName(tableName)
    val tableMetadata = self.getTableMetadata(parsedTableName._1, parsedTableName._2).toVector
    val insertSQL =
      s"""INSERT INTO $tableName (${
        tableMetadata.map({
          _._1
        }).mkString(",")
      })
         |VALUES (${tableMetadata.map({ x => "?" }).mkString(",")})
       """.stripMargin
    val stmt = self.connection.prepareStatement(insertSQL)
    for (row <- csvReader) {
      try {
      assert(row.length == tableMetadata.length, "scheme mismatch")
      for (i <- 1 to tableMetadata.length) {
        tableMetadata(i-1)._2 match {
          case Types.BIGINT => row(i-1) match {
            case "" => stmt.setNull(i,Types.BIGINT)
            case _ => stmt.setLong(i, java.lang.Long.parseLong(row(i-1)))
          }
          case Types.BIT => row(i-1) match {
            case "" => stmt.setNull(i,Types.BIT)
            case _ => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i-1)))
          }
          case Types.BOOLEAN => row(i-1) match {
            case "" => stmt.setNull(i,Types.BOOLEAN)
            case _ => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i-1)))
          }
          case Types.DATE => row(i-1) match {
            case "" => stmt.setNull(i,Types.DATE)
            case _ => stmt.setDate(i, java.sql.Date.valueOf(row(i-1)))
          }
          case Types.DECIMAL => row(i-1) match {
            case "" => stmt.setNull(i,Types.DECIMAL)
            case _ => stmt.setBigDecimal(i, new math.BigDecimal(row(i-1)))
          }
          case Types.DOUBLE => row(i-1) match {
            case "" => stmt.setNull(i,Types.DOUBLE)
            case _ => stmt.setDouble(i, java.lang.Double.parseDouble(row(i-1)))
          }
          case Types.FLOAT => row(i-1) match {
            case "" => stmt.setNull(i,Types.FLOAT)
            case _ => stmt.setFloat(i, java.lang.Float.parseFloat(row(i-1)))
          }
          case Types.INTEGER => row(i-1) match {
            case "" => stmt.setNull(i,Types.INTEGER)
            case _ => stmt.setInt(i, java.lang.Integer.parseInt(row(i-1)))
          }
          case Types.NUMERIC => row(i-1) match {
            case "" => stmt.setNull(i,Types.NUMERIC)
            case _ => stmt.setBigDecimal(i, new math.BigDecimal(row(i-1)))
          }
          case Types.REAL => row(i-1) match {
            case "" => stmt.setNull(i,Types.REAL)
            case _ => stmt.setFloat(i, java.lang.Float.parseFloat(row(i-1)))
          }
          case Types.SMALLINT => row(i-1) match {
            case "" => stmt.setNull(i,Types.SMALLINT)
            case _ => stmt.setShort(i, java.lang.Short.parseShort(row(i-1)))
          }
          case Types.TIME => row(i-1) match {
            case "" => stmt.setNull(i,Types.TIME)
            case _ => stmt.setTime(i, java.sql.Time.valueOf(row(i-1)))
          }
          case Types.TIME_WITH_TIMEZONE => row(i-1) match {
            case "" => stmt.setNull(i,Types.TIME_WITH_TIMEZONE)
            case _ => stmt.setTime(i, java.sql.Time.valueOf(row(i-1)))
          }
          case Types.TINYINT => row(i-1) match {
            case "" => stmt.setNull(i,Types.TINYINT)
            case _ => stmt.setByte(i, java.lang.Byte.parseByte(row(i-1)))
          }
          case _ => row(i-1) match {
            case "" => stmt.setNull(i,tableMetadata(i-1)._2)
            case _ => stmt.setString(i,row(i-1))
          }
        }
      }
        stmt.execute()
      } catch {
        case e: SQLException => {
          rejectedRecordCounter += 1L
          AppLogger debug s"row ${csvReader.rowCounter} insert statement failed"
          errorWriter.writeRow(row)
        }

        case e: AssertionError => {
          rejectedRecordCounter += 1L
          AppLogger debug s"row ${csvReader.rowCounter} schema doesn't match target schema"
          errorWriter.writeRow(row)
        }

        case e: Exception => {
          rejectedRecordCounter += 1L
          AppLogger debug s"row ${csvReader.rowCounter} insert failed"
          errorWriter.writeRow(row)
        }
      }
    }
    errorWriter.close()
    csvReader.rowCounter -> rejectedRecordCounter
  }
}







