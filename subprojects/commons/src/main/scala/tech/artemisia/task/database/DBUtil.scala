package tech.artemisia.task.database

import java.io.{BufferedWriter, File, FileWriter}
import java.sql.ResultSet
import com.opencsv.CSVWriter
import tech.artemisia.core.AppLogger
import tech.artemisia.task.settings.ExportSetting

/**
  * Created by chlr on 4/15/16.
  */

object DBUtil {

   /**
    *
    * @param resultSet ResultSet to be exported
    * @param exportSettings ExportSetting object
    * @todo emit total number of records exported
    * @return total no of rows exported
    */
   def exportCursorToFile(resultSet: ResultSet, exportSettings: ExportSetting): Long = {
     var recordCounter = 0L
     AppLogger info s"exporting result-set to file: ${exportSettings.file.getPath}"
     val buffer = new BufferedWriter(new FileWriter(new File(exportSettings.file)))
     val csvWriter = new CSVWriter(buffer, exportSettings.delimiter,
       if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
     val data = streamResultSet(resultSet, header = exportSettings.header)
     for (record <- data) {
       recordCounter += 1
       csvWriter.writeNext(record)
     }
     buffer.close()
     AppLogger info s"exported $recordCounter rows to ${exportSettings.file.getPath}"
     recordCounter
   }

   /**
    *
    * @param rs input ResultSet
    * @param header include header
    * @return Stream of records represented as array of columns
    */
   private def streamResultSet(rs: ResultSet, header: Boolean = false) = {
     val columnCount = rs.getMetaData.getColumnCount

     def nextRecord: Stream[Array[String]] = {
       if (rs.next()) {
         val record = for ( i <- 1 to columnCount) yield { rs.getString(i) }
         Stream.cons(record.toArray,nextRecord)
       } else {
         Stream.empty
       }
     }

     if (header) {
       val headerRow = for (i <- 1 to columnCount) yield { rs.getMetaData.getColumnLabel(i) }
       Stream.cons(headerRow.toArray, nextRecord)
     }
     else
       nextRecord
   }


  /**
   * takes a tablename literal and parses the optional databasename part and the tablename part
   * @param tableName input table of format <databasename>.<tablename>
   * @return
   */
  def parseTableName(tableName: String): (Option[String],String) = {
      val result  = tableName.split("""\.""").toList
      result.length match {
        case 1 => None -> result.head
        case 2 => Some(result.head) -> result(1)
        case _ => throw new IllegalArgumentException(s"$tableName is a valid table identifier")
      }
  }


 }
