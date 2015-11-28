package org.ultron.core.dag

import com.typesafe.config.{ConfigRenderOptions, Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.config.Keywords
import org.ultron.task.Task

/**
 * Created by chlr on 1/7/16.
 */
object Message {

  trait Messageable

  class Tick extends Messageable

  class TaskCompleted extends Messageable

  case class TaskFailed(name: String, task_stats: TaskStats, exception: Throwable) extends TaskCompleted

  case class TaskWrapper(name: String,task: Task) extends Messageable

  case class TaskSuceeded(name: String, task_stats: TaskStats) extends TaskCompleted


  case class TaskStats  (
                        start_time: String,
                        end_time: String = null,
                        status: Status.Value,
                        attempts: Int = 1,
                        task_output: Config = ConfigFactory.empty()
                        ) extends Messageable {

    def toConfig(task_name: String) = {

      ConfigFactory parseString {
        s"""
          |$task_name = {
          |  ${Keywords.TaskStats.START_TIME} = "$start_time"
          |  ${Keywords.TaskStats.END_TIME} = "$end_time"
          |  ${Keywords.TaskStats.STATUS} = $status
          |  ${Keywords.TaskStats.ATTEMPT} = $attempts
          |  ${Keywords.TaskStats.TASK_OUTPUT} = ${task_output.root().render(ConfigRenderOptions.concise())}
          |}
        """.stripMargin
      }
    }
  }


  object TaskStats {

     def apply(content: Config): TaskStats = {

       TaskStats(content.as[String](Keywords.TaskStats.START_TIME),
                content.as[String](Keywords.TaskStats.END_TIME),
                Status.withName(content.as[String](Keywords.TaskStats.STATUS)),
                content.as[Int](Keywords.TaskStats.ATTEMPT),
                content.as[Config](Keywords.TaskStats.TASK_OUTPUT))
    }
  }

}
