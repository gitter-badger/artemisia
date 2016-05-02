package tech.artemesia.util

import java.io.{FileReader, BufferedReader, File}

import au.com.bytecode.opencsv.CSVReader

/**
 * Created by chlr on 5/1/16.
 */


class CSVFileIterator(file: File) extends Iterator[Array[String]] {
  
  var rowCounter: Int = 0
  private var lastRow: Array[String] = Array()
  private val reader = new CSVReader(new BufferedReader(new FileReader(file)))

  override def hasNext: Boolean = {
    !(lastRow == null)
  }

  override def next(): Array[String] = {
    lastRow = reader.readNext()
    rowCounter += 1
    lastRow
  }

}
