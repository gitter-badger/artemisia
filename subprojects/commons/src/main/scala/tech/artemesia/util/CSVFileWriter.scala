package tech.artemesia.util

import java.io.{BufferedWriter, File, FileWriter}

import com.opencsv.CSVWriter
import tech.artemesia.task.settings.ExportSetting

/**
 * Created by chlr on 5/2/16.
 */
class CSVFileWriter(settings: ExportSetting) {

  var totalRows: Long = 0L
  private val writer = new CSVWriter(new BufferedWriter(new FileWriter(new File(settings.file))), settings.delimiter,
    if (settings.quoting) settings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, settings.escapechar)

  def writeRow(row: Array[String]) = {
    totalRows += 1
    writer.writeNext(row)
  }

}
