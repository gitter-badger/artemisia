package org.ultron.core

/**
 * Created by chlr on 3/24/16.
 */



class ProdEnv extends Env {

  override val osUtil: OSUtil = new ProdOSUtil

  class ProdOSUtil extends OSUtil {

    override def getSystemVariable(variable: String): Option[String] =
      scala.util.Properties.envOrNone(variable)

    override def getSystemProperties(property: String) =
      Option(System.getProperty(property))

  }

}

