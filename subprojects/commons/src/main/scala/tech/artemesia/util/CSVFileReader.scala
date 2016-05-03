package tech.artemesia.util

import java.io.{FileReader, BufferedReader, File}
import com.opencsv.CSVReader
import tech.artemesia.task.settings.CSVSettings

/**
 * Created by chlr on 5/1/16.
 */


class CSVFileReader(file: File, settings: CSVSettings) extends Iterator[Array[String]] {

  var rowCounter: Int = 0
  private var lastRow: Array[String] = Array()
  private val reader = new CSVReader(new BufferedReader(new FileReader(file)), settings.delimiter, )

  def rowCount = reader.getLinesRead

  override def hasNext: Boolean = {
    !(lastRow == null)
  }

  override def next(): Array[String] = {
    lastRow = reader.readNext()
    rowCounter += 1
    lastRow
  }

}
