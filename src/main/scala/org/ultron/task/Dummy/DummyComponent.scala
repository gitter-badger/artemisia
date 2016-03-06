package org.ultron.task.dummy

import com.typesafe.config.Config
import org.ultron.task._

/**
 * Created by chlr on 1/9/16.
 */

class DummyComponent extends Component {

  override def dispatch(task : String, name: String, config: Config) = {
     task match {
       case _ => DummyTask(name, config)
     }
  }
}








