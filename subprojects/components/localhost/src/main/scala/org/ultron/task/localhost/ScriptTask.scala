package org.ultron.task.localhost

import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.task.Task
import org.ultron.task.localhost.util.ProcessRunner

/**
 * Created by chlr on 2/21/16.
 */
class ScriptTask(script: String, cwd: String = System.getProperty("user.home"), env: Map[String,String] = Map()) extends Task {

  val process_runner : ProcessRunner = new ProcessRunner()

  override def setup(): Unit = {}

  override def work(): Config = {
    val result = process_runner.executeInShell(cwd,env) {
      script
    }
    ConfigFactory parseString result.get
  }

  override def teardown(): Unit = {}

}

object ScriptTask {

  def apply(config: Config) = {
    new ScriptTask("echo Hello")
  }

}
