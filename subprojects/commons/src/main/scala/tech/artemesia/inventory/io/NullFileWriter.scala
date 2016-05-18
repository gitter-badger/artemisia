package tech.artemesia.inventory.io

/**
 * Created by chlr on 5/15/16.
 */

/**
 * An empty implem
 */
object NullFileWriter extends FileDataWriter {

  override def writeRow(data: Array[String]): Unit = {}

  override def writeRow(data: String): Unit = {}

  override def close(): Unit = {}

}
