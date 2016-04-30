package tech.artemesia.task

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.util.HoconConfigUtil
import HoconConfigUtil.Handler

/**
 * Created by chlr on 1/26/16.
 */
class TestAdderTask(val num1: Int, val num2: Int, val result: String) extends Task("testTask") {

  override def setup(): Unit = {}
  override def work(): Config = { ConfigFactory parseString s"$result = ${num1 + num2}" }
  override def teardown(): Unit = {}

}

object TestAdderTask {
  def apply(param: Config) = {
    new TestAdderTask(param.as[Int]("num1"),param.as[Int]("num2"),param.as[String]("result_var"))
  }
}


class TestFailTask() extends Task("testTask") {

  override def setup(): Unit = {}
  override def work(): Config = { throw new Exception("FailTask always fail") }
  override def teardown(): Unit = {}

}
