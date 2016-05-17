package tech.artemesia.inventory.io

import java.io.{BufferedReader, File, FileReader}
import com.opencsv.CSVReader
import tech.artemesia.task.settings.LoadSettings

/**
 * Created by chlr on 5/1/16.
 */


class CSVFileReader(settings: LoadSettings) extends Iterator[Array[String]] {

  private var currentRow  = Array[String]()
  private val reader = new CSVReader(new BufferedReader(new FileReader(new File(settings.location))), settings.delimiter,
    settings.quotechar, settings.escapechar, settings.skipRows)

  currentRow = reader.readNext()
  def rowCounter = reader.getLinesRead

  override def hasNext: Boolean = {
    !(currentRow == null)
  }

  override def next(): Array[String] = {
    val row = currentRow
    currentRow = reader.readNext()
    row
  }

}
