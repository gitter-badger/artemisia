package tech.artemesia.task

import com.typesafe.config.Config
import tech.artemesia.core.Keywords.Task

/**
 * Created by chlr on 3/3/16.
 */

/**
 * Component usually represents a Data system such as Database, Spark Cluster or Localhost
 */
trait Component {

  /**
   * returns an instance of [[Task]] configured via the config object
   *
   * {{{
   *   dispatch("ScriptTask","mySampleScriptTask",config)
   * }}}
   *
   *
   * @param task task the Component has to execute
   * @param name name assigned to the instance of the task
   * @param config HOCON config payload with configuration data for the task
   * @return an instance of [[Task]]
   */
  def dispatch(task: String, name: String, config: Config): Task

}
