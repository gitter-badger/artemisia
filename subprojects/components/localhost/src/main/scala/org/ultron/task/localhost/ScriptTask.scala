package org.ultron.task.localhost

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.core.AppLogger
import org.ultron.task.Task
import org.ultron.task.localhost.util.ProcessRunner

/**
 * Created by chlr on 2/21/16.
 */
class ScriptTask(script: String, cwd: String = ScriptTask.Defaults.cwd, env: Map[String,String] = ScriptTask.Defaults.env
                 , parse_output: Boolean = ScriptTask.Defaults.parse_output) extends Task {

  val process_runner : ProcessRunner = new ProcessRunner()

  override def setup(): Unit = {}

  override def work(): Config = {
    AppLogger info s"executing script"
    val result = process_runner.executeInShell(cwd,env) {
      script
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
    val cwd = System.getProperty("user.home")
    val env = Map[String,String]()
    val parse_output = false
  }

  def apply(config: Config) = {
    new ScriptTask (
      script = config.as[String]("script")
     ,cwd = if (config.hasPath("cwd")) config.as[String]("cwd") else ScriptTask.Defaults.cwd
     ,env = if (config.hasPath("env")) config.as[Map[String,String]]("env") else ScriptTask.Defaults.env
     ,parse_output = if (config.hasPath("parse_output")) config.as[Boolean]("env") else ScriptTask.Defaults.parse_output
    )
  }

}
