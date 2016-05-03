package tech.artemesia.util

import java.io.{FileWriter, BufferedWriter, File}

import com.opencsv.CSVWriter
import tech.artemesia.task.settings.CSVSettings

/**
 * Created by chlr on 5/2/16.
 */
class CSVFileWriter(file: File, settings: CSVSettings) {

  var totalRows: Long = 0L
  private val writer = new CSVWriter(new BufferedWriter(new FileWriter(file)), settings.delimiter,
    if (settings.quoting) settings.quotechar else CSVWriter.NO_QUOTE_CHARACTER, settings.escapechar)

  def writeRow(row: Array[String]) = {
    totalRows += 1
    writer.writeNext(row)
  }

}
