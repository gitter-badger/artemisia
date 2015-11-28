package org.ultron.core.dag

import akka.actor.Actor
import com.typesafe.config.{Config, ConfigFactory}
import org.ultron.core.AppLogger
import org.ultron.core.dag.Message._
import org.ultron.util.Util

import scala.util.Try

/**
 * Created by chlr on 1/7/16.
 */
class Worker extends Actor {

  override def receive: Receive = {
    case message: TaskWrapper => {
      AppLogger info s"task ${message.name} has been submitted for execution"
      val start_time = Util.currentTime
      val result: Try[TaskCompleted] = message.task.execute() map {
        result: Config => {
          TaskSuceeded(message.name,TaskStats(start_time,Util.currentTime,message.task.getStatus,message.task.getAttempts,result))
        }
      } recover {
          case th: Throwable => {
            TaskFailed(message.name,TaskStats(start_time,Util.currentTime,Status.FAILED,
            message.task.getAttempts,ConfigFactory.empty()),th)
          }
      }
      sender ! result.get
    }
  }

}
