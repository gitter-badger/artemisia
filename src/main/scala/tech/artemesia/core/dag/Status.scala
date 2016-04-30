package tech.artemesia.core.dag

/**
 * Created by chlr on 1/7/16.
 */
object Status extends Enumeration {
  val READY,RUNNING,SUCCEEDED,FAILED,UNKNOWN,SKIPPED = Value
}

class DagException(message: String) extends Exception(message)

