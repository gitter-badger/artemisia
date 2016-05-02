package tech.artemesia.task.database

import java.io.File
import java.math
import java.sql.Types
import tech.artemesia.task.settings.LoadSettings
import tech.artemesia.util.CSVFileIterator

/**
 * Created by chlr on 5/1/16.
 */

/**
 * A mixin trait 
 */
trait DataLoader {

  self: DBInterface =>

  def loadData(tableName: String, loadSettings: LoadSettings): Int

}

object DataLoader {


  trait DefaultLoader extends DataLoader {

    self: DBInterface =>

    override def loadData(tableName: String, loadSettings: LoadSettings): Int = {
      assert(loadSettings.location.getScheme == "file", "File URI is the only supported URI")
      val csvIterator = new CSVFileIterator(new File(loadSettings.location))
      val parsedTableName = DBUtil.parseTableName(tableName)
      val tableMetadata = self.getTableMetadata(parsedTableName._1, parsedTableName._2).toVector
      val insertSQL =
        s"""INSERT INTO $tableName (${tableMetadata.map({ _._1 }).mkString(",")}})
                                                                                  |VALUES (${tableMetadata.map({ x => "?"}).mkString(",")})
       """.stripMargin
      val stmt = self.connection.prepareStatement(insertSQL)
      for (row <- csvIterator) {
        for (i <- 1 to tableMetadata.length) {
          tableMetadata(i)._2 match {
            case Types.BIGINT => stmt.setLong(i, java.lang.Long.parseLong(row(i)))
            case Types.BIT => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i)))
            case Types.BOOLEAN => stmt.setBoolean(i, java.lang.Boolean.parseBoolean(row(i)))
            case Types.CHAR => stmt.setString(i, row(i))
            case Types.DATE => stmt.setDate(i, java.sql.Date.valueOf(row(i)))
            case Types.DECIMAL => stmt.setBigDecimal(i, new math.BigDecimal(row(i)))
            case Types.DOUBLE => stmt.setDouble(i, java.lang.Double.parseDouble(row(i)))
            case Types.FLOAT => stmt.setFloat(i, java.lang.Float.parseFloat(row(i)))
            case Types.INTEGER => stmt.setInt(i, java.lang.Integer.parseInt(row(i)))
            case Types.LONGNVARCHAR => stmt.setString(i, row(i))
            case Types.LONGVARCHAR => stmt.setString(i, row(i))
            case Types.NCHAR => stmt.setString(i, row(i))
            case Types.NUMERIC => stmt.setBigDecimal(i, new math.BigDecimal(row(i)))
            case Types.NVARCHAR => stmt.setString(i, row(i))
            case Types.REAL => stmt.setFloat(i, java.lang.Float.parseFloat(row(i)))
            case Types.SMALLINT => stmt.setShort(i, java.lang.Short.parseShort(row(i)))
            case Types.TIME => stmt.setTime(i,java.sql.Time.valueOf(row(i)))
            case Types.TIME_WITH_TIMEZONE => stmt.setTime(i,java.sql.Time.valueOf(row(i)))
            case Types.TINYINT => stmt.setByte(i, java.lang.Byte.parseByte(row(i)))
            case Types.VARCHAR => stmt.setString(i, row(i))
            case _ => throw new UnsupportedOperationException(s" unsupported jdbc datatype code
              ${tableMetadata(i)._2} for column ${tableMetadata(1)._1}")
          }
        }
        stmt.execute()
      }
      csvIterator.rowCounter
    }
  }

}



