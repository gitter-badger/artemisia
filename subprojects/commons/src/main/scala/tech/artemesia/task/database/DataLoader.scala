package tech.artemesia.task.database

import java.io.File
import java.math
import java.sql.{SQLException, Types}

import tech.artemesia.core.AppLogger
import tech.artemesia.inventory.io.{CSVFileReader, CSVFileWriter, NullFileWriter}
import tech.artemesia.task.settings.{ExportSetting, LoadSettings}

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
  def loadData(tableName: String, loadSettings: LoadSettings): Long = {

    assert(loadSettings.location.getScheme == "file", "File URI is the only supported URI")

    val csvReader = new CSVFileReader(loadSettings)
    val errorWriter = loadSettings.rejectFile.map( x => new CSVFileWriter(ExportSetting(new File(x).toURI,false,'\u0001',false)) ).getOrElse(NullFileWriter)
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
          case Types.BIGINT => stmt.setLong(i, java.lang.Long.parseLong(row(i-1)))
          case Types.BIT => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i-1)))
          case Types.BOOLEAN => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i-1)))
          case Types.DATE => stmt.setDate(i, java.sql.Date.valueOf(row(i-1)))
          case Types.DECIMAL => stmt.setBigDecimal(i, new math.BigDecimal(row(i-1)))
          case Types.DOUBLE => stmt.setDouble(i, java.lang.Double.parseDouble(row(i-1)))
          case Types.FLOAT => stmt.setFloat(i, java.lang.Float.parseFloat(row(i-1)))
          case Types.INTEGER => stmt.setInt(i, java.lang.Integer.parseInt(row(i-1)))
          case Types.NUMERIC => stmt.setBigDecimal(i, new math.BigDecimal(row(i-1)))
          case Types.REAL => stmt.setFloat(i, java.lang.Float.parseFloat(row(i-1)))
          case Types.SMALLINT => stmt.setShort(i, java.lang.Short.parseShort(row(i-1)))
          case Types.TIME => stmt.setTime(i, java.sql.Time.valueOf(row(i-1)))
          case Types.TIME_WITH_TIMEZONE => stmt.setTime(i, java.sql.Time.valueOf(row(i-1)))
          case Types.TINYINT => stmt.setByte(i, java.lang.Byte.parseByte(row(i-1)))
          case _ => stmt.setString(i,row(i-1))
        }
      }
        stmt.execute()
      } catch {
        case e: SQLException => {
          AppLogger debug s"row ${csvReader.rowCounter} insert failed"
          errorWriter.writeRow(row)
        }

        case e: AssertionError => {
          AppLogger debug s"row ${csvReader.rowCounter} schema doesn't match target schema"
          errorWriter.writeRow(row)
        }

        case e: Exception => {
          AppLogger debug s"row ${csvReader.rowCounter} insert failed"
          errorWriter.writeRow(row)
        }
      }
    }
    csvReader.rowCounter
  }
}





