package tech.artemesia

/**
 * Created by chlr on 12/9/15.
 */
package object core {

  var env: Env = new ProdEnv

  class UnKnownTaskException(message: String) extends Exception(message)

}
