package org.ultron.task

import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.TestSpec
import org.ultron.core.dag.Status

import scala.concurrent.duration._
/**
 * Created by chlr on 1/23/16.
 */
class GenericTaskSpec extends TestSpec {


  "The Task" must "retry a failing task for configured number of times" in {
    val task_config = GenericTaskSpec.getDefaultTaskConfig

    val task = new TaskHandler(task_config) {
      override protected def setup(): Unit = {}
      override protected def work(): Config = { throw new Exception("my own exception") }
      override protected def teardown(): Unit = {}
    }

    val result = task.execute()
    result.isFailure must be (true)
    result.failed.get mustBe an [Exception]
    task.getAttempts must be (3)
    task.getStatus must be (Status.FAILED)
  }

  it must "stop retrying if it succeeds before retry limit" in {

    val task_config = GenericTaskSpec.getDefaultTaskConfig
     val task = new TaskHandler(task_config) {
       override protected def setup(): Unit = {}
       override protected def work(): Config = { if (this.getAttempts == 1) { throw new Exception("") } else { ConfigFactory.empty() } }
       override protected def teardown(): Unit = {}
     }
    task.execute()
    task.getAttempts must be (2)
  }

  it must "ignore failures in teardown phase" in {
    val task_config = GenericTaskSpec.getDefaultTaskConfig
    val task = new TaskHandler(task_config) {
      override protected def setup(): Unit = {}
      override protected def work(): Config = {  ConfigFactory.empty }
      override protected def teardown(): Unit = { throw new Exception("my own exception") }
    }
    val result = task.execute()
    result.get must equal (ConfigFactory.empty)
  }

  it must "skip execution if the task_option flag is set" in {
    val task_config = TaskConfig(task_name = "test_task",retry_limit = 3, cooldown = 10 milliseconds, skip_execution = true)
    val task = new TaskHandler(task_config) {
      override protected def setup(): Unit = { throw new Exception("my own exception") }
      override protected def work(): Config = {  throw new Exception("my own exception") }
      override protected def teardown(): Unit = { throw new Exception("my own exception") }
    }
    val result = task.execute()
    task.getStatus must be (Status.SKIPPED)
    task.getAttempts must be (0)
    result.get must equal (ConfigFactory.empty)
  }


}


object GenericTaskSpec {

  def getDefaultTaskConfig = {
     TaskConfig(task_name = "test_task",retry_limit = 3, cooldown = 10 milliseconds)
  }

}
