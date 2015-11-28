package org.ultron.core.dag

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.ultron.config.{AppContext, Keywords}
import org.ultron.core.{AppLogger, dag, wire}
import org.ultron.task.{Component, Task}
import scaldi.Injectable._

import scala.collection.LinearSeq

/**
 * Created by chlr on 1/4/16.
 */

class Node(val name: String, var payload: Config) {

  /* this variable is expected to be handled by actors
     access outside of the actor system might coroupt the state of the node
  */

  private var status = Status.READY
  val ignore_failure: Boolean = false
  var parents: LinearSeq[Node] = Nil

  def isRunnable = {
    (parents forall { node => {
        node.status == dag.Status.SUCCEEDED || node.status == dag.Status.SKIPPED
      }
    }) && this.status == dag.Status.READY // forall for Nil returns true
  }

  def getNodeTask(app_context: AppContext): Task = {
    val component = inject[Component] (payload.getString(Keywords.Task.COMPONENT))
    component.getTask(payload.as[String](Keywords.Task.TASK),name,payload,app_context)
  }

  override def equals(that: Any): Boolean = {
    that match {
      case that: Node => that.name == this.name
      case _ => false
    }
  }


  def setStatus(status: Status.Value): Unit = {
    AppLogger info s"node $name status set to $status"
    this.status = status
  }

  def getStatus = status

  def applyStatusFromCheckpoint(checkpoint_status: Status.Value): Unit = {
    if (status == Status.SUCCEEDED || status == Status.SKIPPED) {
      AppLogger info s"applying checkpoint status $checkpoint_status for node $name"
      this.status = checkpoint_status
    }
    checkpoint_status match {
      case Status.SUCCEEDED => {
        AppLogger info s"marking node $name status as $checkpoint_status from checkpoint"
        this.status = checkpoint_status
      }
      case Status.SKIPPED => {
        AppLogger info s"marking node $name status as $checkpoint_status from checkpoint"
        this.status = checkpoint_status
      }
      case Status.FAILED => {
        AppLogger info s"detected node $name from checkpoint has ${Status.FAILED}. setting node status as ${Status.READY}"
      }
      case _ => {
        AppLogger warn s"node $name has an unhandled status of $checkpoint_status}. setting node status as ${Status.READY}"
      }
    }
  }

  override def toString = {
    s"$name"
  }
}

object Node {

  def apply(name: String, body: Config) = {
    new Node(name,body)
  }

  def apply(name: String) = {
    new Node(name,ConfigFactory.empty())
  }

  def unapply(node: Node) = {
    Some(node.name, node.parents)
  }

}
