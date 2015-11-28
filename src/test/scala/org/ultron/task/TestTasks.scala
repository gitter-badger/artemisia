package org.ultron.task

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._

/**
 * Created by chlr on 1/26/16.
 */
class TestAdderTask(val num1: Int, val num2: Int, val result: String ,task_config :TaskConfig) extends Task(task_config) {

  override protected def setup(): Unit = {}
  override protected def work(): Config = { ConfigFactory parseString s"$result = ${num1 + num2}" }
  override protected def teardown(): Unit = {}

}

object TestAdderTask {
  def apply(param: Config, task_config: TaskConfig) = {
    new TestAdderTask(param.as[Int]("num1"),param.as[Int]("num2"),param.as[String]("result_var"),task_config)
  }
}


class TestFailTask(task_config :TaskConfig) extends Task(task_config) {

  override protected def setup(): Unit = {}
  override protected def work(): Config = { throw new Exception("FailTask always fail") }
  override protected def teardown(): Unit = {}

}
