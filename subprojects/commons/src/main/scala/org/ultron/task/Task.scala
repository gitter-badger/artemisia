package org.ultron.task

import java.io.{File, PrintWriter}

import com.google.common.io.Files
import com.typesafe.config.Config
import org.ultron.core.LogSource
import org.ultron.util.FileSystemUtil

import scala.io.Source

/**
 * Created by chlr on 3/3/16.
 */

abstract class Task(val task_name: String) {

  implicit protected var source: LogSource = null

  private[task] def setLogSource(source: LogSource) = {
    this.source = source
  }

  protected def writeToFile(content: String, file_name: String) = {
    val file = this.getFileHandle(file_name)
    Files.createParentDirs(file)
    val writer = new PrintWriter(file)
    writer.write(content)
    writer.close()
    file
  }

  protected def getFileHandle(file_name: String) = {
    new File(FileSystemUtil.joinPath(TaskContext.getWorkingDir.toString,task_name),file_name)
  }

  protected def readFile(file_name: String) = {
    Source.fromFile(getFileHandle(file_name)).getLines().mkString
  }

  protected[task] def setup(): Unit
  protected[task] def work(): Config
  protected[task] def teardown(): Unit

  def execute: Config = {
    this.setup()
    val result = this.work()
    this.teardown()
    result
  }

}

