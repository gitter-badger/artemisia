package org.ultron.util

import java.util.concurrent.locks.{Lock, ReentrantLock}

import scala.collection.mutable

/**
 * Created by chlr on 12/9/15.
 */
class OSUtilTestImpl extends OSUtil {

  private[this] val systemVariableMap: mutable.Map[String,String] = mutable.Map()
  private[this] val systemPropertiesMap: mutable.Map[String,String] = mutable.Map("user.home" -> System.getProperty("user.home"))
  private[this] val lock :Lock = new ReentrantLock()

  override def getSystemVariable(variable: String): Option[String] = {
    systemVariableMap get variable
  }

  def getSystemProperties(variable: String): Option[String] = {
    systemPropertiesMap get variable
  }

  def withSysVar(map: scala.collection.Map[String,String])(body : => Unit): Unit = {
    try {
      lock.lock()
      systemVariableMap.clear()
      systemVariableMap ++= map.toSeq
      body
    }
    catch {
      case e: Throwable => throw e
    }
    finally {
      lock.unlock()
    }
  }

}
