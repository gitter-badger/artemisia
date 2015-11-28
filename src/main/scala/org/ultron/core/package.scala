package org.ultron

import org.ultron.task.Component
import org.ultron.task.dummy.DummyComponent
import org.ultron.util.{OSUtil, OSUtilImpl}
import scaldi.{Injector, Module}

/**
 * Created by chlr on 12/9/15.
 */
package object core {
  implicit var wire: Injector = getWireObject

  def getWireObject = {
    new Module {
      bind[OSUtil] to new OSUtilImpl
      bind[Component] identifiedBy "Dummy" to new DummyComponent
    }
  }
}
