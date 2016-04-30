package tech.artemesia.core

import java.io.File

/**
 * Created by chlr on 3/24/16.
 */

trait Env {

  val osUtil: OSUtil

  trait OSUtil {

    def joinPath(path: String*) = {
      path.foldLeft(System.getProperty("file.separator"))((a: String, b: String) => new File(a, b).toString)
    }

    def getSystemVariable(variable: String): Option[String]

    def getSystemProperties(variable: String): Option[String]

  }

}


