package org.ultron.task.dummy

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.core.AppLogger
import org.ultron.task.Task

/**
 * Created by chlr on 1/9/16.
 */


class DummyTask(val dummy_param1: Int , val dummy_param2: Boolean) extends Task {

  override def setup(): Unit = {
    AppLogger info s"IN SETUP with $dummy_param1 and $dummy_param2"
  }

  override def work(): Config = {
    Thread.sleep(1000)
    AppLogger info s"In Work  $dummy_param1 and $dummy_param2"
    ConfigFactory parseString
      s"""
        | new_variable = 1000
      """.stripMargin
  }

  override def teardown(): Unit = {
    AppLogger info s"In Teardown  $dummy_param1 and $dummy_param2"
  }

}

object DummyTask {

  val default_config = ConfigFactory parseString {
    """
      | dummy_param2 = yes
    """.stripMargin
  }

  def apply(input_config: Config) = {
    val config = input_config withFallback default_config
    new DummyTask(config.as[Int]("dummy_param1"),config.as[Boolean]("dummy_param2"))
  }
}
