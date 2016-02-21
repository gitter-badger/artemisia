package org.ultron.task

import com.typesafe.config.{ConfigFactory, Config}
import org.ultron.config.Keywords
import org.ultron.core.AppLogger
import org.ultron.core.dag.Status
import org.ultron.logging.LogSource
import org.ultron.util.Util

import scala.util.{Failure, Success, Try}


/**
 * Created by chlr on 1/7/16.
 */

class TaskHandler(val task_metadata: TaskConfig, val task: Task) {

  task.setLogSource(LogSource(task_metadata.task_name))
  private var attempts = 0
  private var status: Status.Value = Status.UNKNOWN

  final def execute(): Try[Config] = {
    if(!task_metadata.skip_execution) {
      AppLogger info s"running task with total allowed attempts of ${task_metadata.retry_limit}"
      val result = run(max_attempts = task_metadata.retry_limit) {
          AppLogger debug "executing setup phase of the task"
          task.setup()
          AppLogger debug "executing work phase of the task"
          task.work()
      }
      try {
        AppLogger debug "executing teardown phase of the task"
        task.teardown()
      } catch {
        case ex: Throwable => AppLogger warn s"teardown phase failed with exception ${ex.getClass.getCanonicalName} with message ${ex.getMessage}"
      }
      result
    } else {
      AppLogger info s"skipping execution of ${task_metadata.task_name}. ${Keywords.Task.SKIP_EXECUTION} flag is set"
      status = Status.SKIPPED
      Success(ConfigFactory.empty())
    }
  }

  private def run(max_attempts: Int)(body : => Config) : Try[Config] = {
    try {
      attempts += 1
      val result = body
      this.status = Status.SUCCEEDED
      Success(result)
    } catch {
      case ex: Throwable => {
        AppLogger info s"attempt ${task_metadata.retry_limit - max_attempts + 1} for task ${task_metadata.task_name}"
        AppLogger error Util.printStackTrace(ex)
        if (max_attempts > 1) {

          run(max_attempts -1)(body)
        }
        else {
          status = Status.FAILED
          Failure(ex)
        }
      }
    }
  }

  def getAttempts = attempts

  def getStatus = status

}

