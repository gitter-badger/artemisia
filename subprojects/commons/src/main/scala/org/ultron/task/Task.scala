package org.ultron.task

import com.typesafe.config.Config
import org.ultron.logging.LogSource

/**
 * Created by chlr on 3/3/16.
 */
trait Task {

  implicit protected var source: LogSource = null

  private[task] def setLogSource(source: LogSource) = {
    this.source = source
  }

  def setup(): Unit
  def work(): Config
  def teardown(): Unit

}
