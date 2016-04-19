package org.ultron.util.db

import java.io.{BufferedWriter, FileWriter}
import java.sql.ResultSet

import au.com.bytecode.opencsv.CSVWriter
import org.ultron.core.AppLogger

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
  def exportCursorToFile(resultSet: ResultSet, exportSettings: ExportSetting) = {
    var recordCounter = 0
    AppLogger info s"exporting result-set to file: ${exportSettings.file.getName}"
    val buffer = new BufferedWriter(new FileWriter(exportSettings.file))
    val csvWriter = new CSVWriter(buffer, exportSettings.delimiter,
      if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
    val data: Stream[Array[String]] = streamResultSet(resultSet, header = exportSettings.header)
    for (record <- data) {
      recordCounter += 1
      csvWriter.writeNext(record)
    }
    buffer.close()
    AppLogger info s"exported $recordCounter rows to ${exportSettings.file.getAbsolutePath}"
    recordCounter
  }


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


}
