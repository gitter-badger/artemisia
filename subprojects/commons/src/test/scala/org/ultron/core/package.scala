package org.ultron

import org.ultron.util.{OSUtil, OSUtilTestImpl}
import scaldi.{Injector, Module}

/**
 * Created by chlr on 1/1/16.
 */
package object core {
  implicit var wire: Injector = getWireObject

  def getWireObject = {
    new Module {
      bind[OSUtil] to new OSUtilTestImpl
    }
  }
}
