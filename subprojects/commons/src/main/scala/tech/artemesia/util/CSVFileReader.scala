package tech.artemesia.util

import java.io.{BufferedReader, File, FileReader}

import com.opencsv.CSVReader
import tech.artemesia.task.settings.LoadSettings

/**
 * Created by chlr on 5/1/16.
 */


class CSVFileReader(file: File, settings: LoadSettings) extends Iterator[Array[String]] {

  private var lastRow: Array[String] = Array()
  private val reader = new CSVReader(new BufferedReader(new FileReader(file)), settings.delimiter, settings.quotechar,
    settings.escapechar, settings.skipRows)

  def rowCounter = reader.getLinesRead

  override def hasNext: Boolean = {
    !(lastRow == null)
  }

  override def next(): Array[String] = {
    lastRow = reader.readNext()
    lastRow
  }

}
