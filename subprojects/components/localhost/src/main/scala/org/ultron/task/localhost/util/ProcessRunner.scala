package org.ultron.task.localhost.util

import java.io.{BufferedReader, File, InputStreamReader, StringWriter}

import scala.collection.JavaConversions._
/**
 * Created by chlr on 2/23/16.
 */
class ProcessRunner(val interpreter: String = "/bin/sh") {

  def executeInShell(cwd: String = System.getProperty("user.home"), env: Map[String, String] = Map())(body : String): (String,String,Int) = {
    val pb = new  ProcessBuilder()
    val output = new StringWriter()
    pb.directory(new File(cwd))
    pb.redirectOutput(ProcessBuilder.Redirect.PIPE)
    pb.redirectError(ProcessBuilder.Redirect.PIPE)
    val env_variables = pb.environment()
    env map { vars => env_variables.put(vars._1,vars._2) }
    pb.command(interpreter :: "-c" :: body.split(System.getProperty("line.separator")).toList)
    this.execute(pb)
  }

  private def execute(pb: ProcessBuilder) : (String,String,Int) = {
    val process = pb.start()
    val stdout_buffer = new BufferedReader( new InputStreamReader(process.getInputStream))
    val stderr_buffer = new BufferedReader( new InputStreamReader(process.getErrorStream))
    val stdout = Stream.continually(stdout_buffer.readLine()).takeWhile(_ != null).mkString(System.getProperty("line.separator"))
    val stderr = Stream.continually(stderr_buffer.readLine()).takeWhile(_ != null).mkString(System.getProperty("line.separator"))
    val return_code = process.waitFor()
    (stdout,stderr,return_code)
  }

}
