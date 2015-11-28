package org.ultron.core.dag

import akka.actor.Props
import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.ActorTestSpec
import org.ultron.core.dag.Message.{TaskFailed, TaskStats, TaskSuceeded, TaskWrapper}
import org.ultron.task.{GenericTaskSpec, Task}

import scala.concurrent.duration._
/**
 * Created by chlr on 1/25/16.
 */
class WorkerActorSpec extends ActorTestSpec {

  "The worker" must "send TaskSuceeded message when the task completes successfully" in {

    val worker = system.actorOf(Props[Worker])
    val test_task = new Task(GenericTaskSpec.getDefaultTaskConfig) {

      override protected def setup(): Unit = {}
      override protected def work(): Config = {ConfigFactory.empty()}
      override protected def teardown(): Unit = {}
    }
    within(1 seconds) {
      worker ! TaskWrapper("test_task", test_task)
      expectMsgPF(1 second) {
        case TaskSuceeded("test_task",TaskStats(_,_,Status.SUCCEEDED,1,_)) => ()
      }
    }

  }

  it must "send TaskFailed message when the task fails" in {

    val worker = system.actorOf(Props[Worker])
    val test_task = new Task(GenericTaskSpec.getDefaultTaskConfig) {

      override protected def setup(): Unit = { throw new Exception("my known exception") }
      override protected def work(): Config = {ConfigFactory.empty()}
      override protected def teardown(): Unit = {}
    }
    within(1 seconds) {
      worker ! TaskWrapper("test_task", test_task)
      expectMsgPF(1 second) {
        case TaskFailed("test_task",TaskStats(_,_,Status.FAILED,3,_),_) => ()
      }
    }

  }

}
