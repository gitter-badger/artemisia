package org.ultron.util.db

import java.io.{BufferedWriter, FileWriter}
import java.sql.ResultSet
import au.com.bytecode.opencsv.CSVWriter
import org.ultron.core.AppLogger
import scala.collection._
import scala.collection.convert.wrapAsJava

/**
 * Created by chlr on 4/15/16.
 */

object DBUtil {

  /**
   *
   * @param resultSet ResultSet to be exported
   * @param exportSettings
   * @todo emit total number of records exported
   */
  def exportCursorToFile(resultSet: ResultSet, exportSettings: ExportSetting) = {
    // TODO: emit total no of records emitted
    AppLogger info s"exporting resultset to file: ${exportSettings.file.getName}"
    val buffer = new BufferedWriter(new FileWriter(exportSettings.file))
    val csvWriter = new CSVWriter(buffer, exportSettings.delimiter,
      if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
    val data = resultSetToList(resultSet, header = exportSettings.header)
    csvWriter.writeAll(wrapAsJava.seqAsJavaList(data))
    buffer.close()
    data.length
  }

  def exportCursorToFile(result: List[Array[String]], exportSettings: ExportSetting) = {
    AppLogger info s"exporting data to file: ${exportSettings.file.getName}"
    val csvWriter = new CSVWriter(new FileWriter(exportSettings.file), exportSettings.delimiter,
      if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
    val newlist: java.util.List[Array[String]] = wrapAsJava.seqAsJavaList(result)
    csvWriter.writeAll(newlist)
  }

  private def resultSetToList(rs: ResultSet, header: Boolean = false) = {
    val data = mutable.ListBuffer[Array[String]]()
    val columnCount = rs.getMetaData.getColumnCount
    if (header) {
      val headerData: Seq[String] = for(i <- 1 to columnCount) yield {
        rs.getMetaData.getColumnLabel(i)
      }
      data += headerData.toArray
    }
    while(rs.next()) {
      val record: Seq[String] = for ( i <- 1 to columnCount) yield {
        rs.getString(i)
      }
      data += record.toArray
    }
    data
  }



}
