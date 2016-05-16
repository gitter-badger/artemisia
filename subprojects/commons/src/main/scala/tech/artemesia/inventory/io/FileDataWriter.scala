package tech.artemesia.inventory.io

/**
 * Created by chlr on 5/15/16.
 */

abstract class FileDataWriter {

  def writeRow(data: Array[String])

  def writeRow(data: String)

}
