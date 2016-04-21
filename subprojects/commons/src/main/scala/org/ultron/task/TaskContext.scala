package org.ultron.task

import java.nio.file.Path

import com.google.common.io.Files
import com.typesafe.config.Config
import org.ultron.util.db.ConnectionProfile

import scala.collection.JavaConverters._

/**
 * Created by chlr on 3/7/16.
 */


/**
 * This object is used to hold contextual information required for Task execution.
 *
 * properties such as common working directory for all tasks which doesn't qualify to an task attribute but still required for task execution goes here.
 */
private[ultron] object TaskContext {

  private var preferredWorkingDir: Option[Path] = None
  var predefinedConnectionProfiles: Map[String,ConnectionProfile] = Map()

  /**
   *  the attribute that holds the working directory
   */
  lazy val workingDir = preferredWorkingDir.getOrElse(Files.createTempDir().toPath)


  /**
   * set the working directory to be used.
   *
   * '''ensure that setWorkingDir is invoked before workingDir variable is accessed'''
   * if workingDir is accessed before setWorkingDir is set, then a random tmp directory is assigned and any further 
   * assignment of working directory via setWorkingDir will not have any effect. 
   * @param working_dir the working directory to be used
   */
  def setWorkingDir(working_dir: Path) = {
    preferredWorkingDir = Some(working_dir)
  }


  def parseConnections(connectionConfigs: Config) = {
    val connections = connectionConfigs.entrySet.asScala map {
      x => {
        x.getKey -> ConnectionProfile(x.getValue.asInstanceOf[Config])
      }
    }
    connections.toMap
  }

}


