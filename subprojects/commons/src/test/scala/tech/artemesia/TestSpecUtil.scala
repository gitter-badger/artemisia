package tech.artemesia

import java.io.File

/**
 * Created by chlr on 5/13/16.
 */

object TestSpecUtil {

  def withFile(fileName: String)(body: File => Unit): Unit = {
    val file = File.createTempFile(fileName, "tmp")
    body(file)
    file.delete()
  }

}
