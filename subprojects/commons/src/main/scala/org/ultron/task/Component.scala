package org.ultron.task

import com.typesafe.config.Config

/**
 * Created by chlr on 3/3/16.
 */
trait Component {
  def dispatch(task: String, name: String, config: Config): Task
}
