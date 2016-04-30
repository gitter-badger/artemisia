package tech.artemesia.task

import com.typesafe.config.{Config, ConfigFactory}
import tech.artemesia.core.Keywords.Task
import tech.artemesia.core.dag.Status
import tech.artemesia.core.{AppLogger, LogSource}
import tech.artemesia.util.Util
import scala.util.{Failure, Success, Try}


/**
 * Created by chlr on 1/7/16.
 */

class TaskHandler(val taskMetadata: TaskConfig, val task: Task) {

  task.setLogSource(LogSource(taskMetadata.taskName))
  private var attempts = 0
  private var status: Status.Value = Status.UNKNOWN

  final def execute(): Try[Config] = {
    if(!taskMetadata.skipExecution) {
      AppLogger info s"running task with total allowed attempts of ${taskMetadata.retryLimit}"
      val result = run(maxAttempts = taskMetadata.retryLimit) {
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
      AppLogger info s"skipping execution of ${taskMetadata.taskName}. ${Task.SKIP_EXECUTION} flag is set"
      status = Status.SKIPPED
      Success(ConfigFactory.empty())
    }
  }

  private def run(maxAttempts: Int)(body : => Config) : Try[Config] = {
    try {
      attempts += 1
      val result = body
      this.status = Status.SUCCEEDED
      Success(result)
    } catch {
      case ex: Throwable => {
        AppLogger info s"attempt ${taskMetadata.retryLimit - maxAttempts + 1} for task ${taskMetadata.taskName}"
        AppLogger error Util.printStackTrace(ex)
        if (maxAttempts > 1) {

          run(maxAttempts -1)(body)
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

