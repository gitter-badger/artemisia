package org.ultron.task.localhost


import java.nio.file.Paths
import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.core.AppLogger
import org.ultron.task.localhost.util.ProcessRunner
import org.ultron.task.Task
import org.ultron.util.Util

/**
 * Created by chlr on 2/21/16.
 */
class ScriptTask(name: String = Util.getUUID, script: String,interpreter: String = ScriptTask.Defaults.interpreter ,cwd: String = ScriptTask.Defaults.cwd
                 , env: Map[String,String] = ScriptTask.Defaults.env
                 , parse_output: Boolean = ScriptTask.Defaults.parse_output) extends Task(name: String) {


  val process_runner : ProcessRunner = new ProcessRunner(interpreter)
  val script_file_name = "script.sh"

  override def setup(): Unit = {
    this.writeToFile(script,script_file_name)
  }

  override def work(): Config = {
    AppLogger info s"executing script"
    val result = process_runner.executeFile(cwd,env) {
      this.getFileHandle(script_file_name).toPath.toAbsolutePath.toString
    }
    if (result._1.length > 0)
      AppLogger debug s"stdout decteced: ${result._1}"
    if (result._2.length > 0)
      AppLogger debug s"stderr decteced: ${result._2}"
    assert(result._3 == 0, "Non Zero return code detected")
      ConfigFactory parseString {
        if (parse_output) result._1 else ""
      }
  }

  override def teardown(): Unit = {}

}

object ScriptTask {

  object Defaults {
    val cwd = Paths.get("").toAbsolutePath.toString
    val env = Map[String,String]()
    val parse_output = false
    val interpreter = "/bin/sh"
  }

  def apply(name: String,config: Config) = {
    new ScriptTask (
      name
     ,script = config.as[String]("script")
     ,interpreter = if (config.hasPath("cwd")) config.as[String]("interpreter") else ScriptTask.Defaults.interpreter
     ,cwd = if (config.hasPath("cwd")) config.as[String]("cwd") else ScriptTask.Defaults.cwd
     ,env = if (config.hasPath("env")) config.as[Map[String,String]]("env") else ScriptTask.Defaults.env
     ,parse_output = if (config.hasPath("parse_output")) config.as[Boolean]("env") else ScriptTask.Defaults.parse_output
    )
  }

}
