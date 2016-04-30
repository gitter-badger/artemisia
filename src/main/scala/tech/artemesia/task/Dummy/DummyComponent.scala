package tech.artemesia.task.Dummy

import com.typesafe.config.Config
import tech.artemesia.task.Component

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








