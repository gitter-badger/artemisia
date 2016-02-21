package org.ultron.task.localhost.util

import java.io.{InputStreamReader, BufferedReader, StringWriter, File}
import scala.util.Try

/**
 * Created by chlr on 2/23/16.
 */
class ProcessRunner(val interpreter: String = "/bin/sh") {

  def executeInShell(cwd: String = System.getProperty("user.home"), env: Map[String, String] = Map())(body : String): Try[String] = {
    val pb = new  ProcessBuilder()
    val output = new StringWriter()
    pb.directory(new File(cwd))
    pb.redirectOutput()
    val env_variables = pb.environment()
    env map { vars => env_variables.put(vars._1,vars._2) }
    pb.command(s"$interpreter -c ${body.mkString(" ; ")}")
    Try(this.execute(pb))
  }

  private def execute(pb: ProcessBuilder) : String = {
    val process = pb.start()
    val buffer = new BufferedReader( new InputStreamReader(process.getInputStream))
    val content = Stream.continually(buffer.readLine()).takeWhile(_ != null).mkString("\n")
    assert(process.waitFor() == 0, "non-zero exit code")
    content
  }

}
