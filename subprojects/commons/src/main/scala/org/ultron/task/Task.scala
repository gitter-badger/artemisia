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

  protected[task] def setup(): Unit
  protected[task] def work(): Config
  protected[task] def teardown(): Unit


}
