package tech.artemisia.core

/**
 * Created by chlr on 3/24/16.
 */

trait Env {

  val osUtil: OSUtil

  trait OSUtil {


    def getSystemVariable(variable: String): Option[String]

    def getSystemProperties(variable: String): Option[String]

  }

}


