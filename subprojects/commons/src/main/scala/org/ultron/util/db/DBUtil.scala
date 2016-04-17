package org.ultron.util.db

import java.io.FileWriter
import java.sql.ResultSet
import au.com.bytecode.opencsv.CSVWriter
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
  def exportCursorToFile(resultSet: ResultSet, exportSettings: ExportSettings) = {
    // TODO: emit total no of records emitted
    val csvWriter = new CSVWriter(new FileWriter(exportSettings.file), exportSettings.delimiter,
      if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
    csvWriter.writeAll(resultSet,exportSettings.includeHeader)
  }

  def exportCursorToFile(result: List[Array[String]], exportSettings: ExportSettings) = {
    val csvWriter = new CSVWriter(new FileWriter(exportSettings.file), exportSettings.delimiter,
      if (exportSettings.quoting) exportSettings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, exportSettings.escapechar)
    val newlist: java.util.List[Array[String]] = wrapAsJava.seqAsJavaList(result)
    csvWriter.writeAll(newlist)
  }

}
