package org.ultron.task.dummy

import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.util.HoconConfigUtil.Handler
import org.ultron.core.AppLogger
import org.ultron.task.Task
import org.ultron.util.Util


/**
 * Created by chlr on 1/9/16.
 */


class DummyTask(name: String = Util.getUUID,val dummyParam1: Int , val dummyParam2: Boolean) extends Task(name) {

  override def setup(): Unit = {
    AppLogger info s"IN SETUP with $dummyParam1 and $dummyParam2"
  }

  override def work(): Config = {
    Thread.sleep(1000)
    AppLogger info s"In Work  $dummyParam1 and $dummyParam2"
    ConfigFactory parseString
      s"""
        | new_variable = 1000
      """.stripMargin
  }

  override def teardown(): Unit = {
    AppLogger info s"In Teardown  $dummyParam1 and $dummyParam2"
  }

}

object DummyTask {

  val default_config = ConfigFactory parseString {
    """
      | dummy_param2 = yes
    """.stripMargin
  }

  def apply(name: String,inputConfig: Config) = {
    val config = inputConfig withFallback default_config
    new DummyTask(name,config.as[Int]("dummy_param1"),config.as[Boolean]("dummy_param2"))
  }
}
