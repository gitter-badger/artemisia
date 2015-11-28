package org.ultron.config



import com.typesafe.config.{ConfigObject, Config, ConfigFactory, ConfigRenderOptions}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import org.ultron.core.AppLogger
import org.ultron.core.dag.Message.TaskStats
import org.ultron.util.Util
import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration
import scala.collection.JavaConversions._


/**
*  Created by chlr on 11/28/15.
*/


class AppContext(cmd_line_param: AppSetting) {

  private[config] case class DagSetting(attempts: Int, concurrency: Int, heartbeat_cycle: FiniteDuration,
                                        cooldown: FiniteDuration, disable_assertions: Boolean, ignore_conditions: Boolean)
  private[config] case class CoreSetting(working_dir: String)
  private[config] case class Logging(console_trace_level: String, file_trace_level: String)
  

  val skip_checkpoints = cmd_line_param.skip_checkpoints
  val global_config_file = Util.getGlobalConfigFileLocation()
  var payload = getConfigObject(cmd_line_param)
  val logging: Logging = payload.as[Logging]("__setting__.logging")
  val dag_setting: DagSetting = payload.as[DagSetting]("__setting__.dag")
  val core_setting: CoreSetting = payload.as[CoreSetting]("__setting__.core")
  val run_id: String = cmd_line_param.run_id.getOrElse(Util.getUUID)
  val working_dir: String = cmd_line_param.working_dir.getOrElse(Util.joinPath(payload.getString("__setting__.core.working_dir"),run_id))
  val checkpoints: mutable.Map[String,TaskStats] = if (skip_checkpoints) mutable.Map() else readCheckpoint
  

  private[config] def getConfigObject(cmd_line_param: AppSetting): Config = {
    val empty_object = ConfigFactory.empty()
    val reference = ConfigFactory parseString Util.readResource("/reference.conf")
    val context = (cmd_line_param.context map ( ConfigFactory parseString _ )).getOrElse(empty_object)
    val config_file = (cmd_line_param.config map (Util readConfigFile)).getOrElse(empty_object)
    val code = (cmd_line_param.cmd filter { _ == "run" } map
      { x => Util.readConfigFile(cmd_line_param.value.get) }).getOrElse(empty_object)
    val global_config_option = (global_config_file map { Util readConfigFile}).getOrElse(empty_object)
    context withFallback config_file withFallback code withFallback global_config_option withFallback reference
  }


  override def toString = {
    val options = ConfigRenderOptions.defaults() setComments false setFormatted true setOriginComments false setJson true
    payload.root().render(options)
  }


  def checkpointFile = Util.joinPath(working_dir,Keywords.Config.CHECKPOINT_FILE)

  protected[config] def readCheckpoint: mutable.Map[String,TaskStats] = {
    val checkpoints = mutable.Map[String,TaskStats]()
    if (Util.fileExists(checkpointFile)) {
      AppLogger info s"checkpoint file $checkpointFile detected"
      Util.readConfigFile(checkpointFile).root() foreach {
        case (key,value: ConfigObject) => checkpoints += (key -> TaskStats(value.toConfig))
      }
    }
    checkpoints
  }

  def writeCheckpoint(task_name: String, task_stats: TaskStats): Unit = {

    checkpoints += (task_name -> task_stats)
    payload = task_stats.task_output withFallback payload

    val content = checkpoints.foldLeft(ConfigFactory.empty()) {
        (cp_config: Config, cp: (String, TaskStats)) => {
        ConfigFactory.parseString(cp._2.toConfig(cp._1).root().render(ConfigRenderOptions.concise())) withFallback cp_config
      }
    }
    if (!this.skip_checkpoints) {
      Util.writeFile(content.root().render(ConfigRenderOptions.concise()), checkpointFile, append = false)
    }
  }

}

case class AppSetting(cmd: Option[String] = Some("run"), value: Option[String] = None, context: Option[String] = None
                         , config: Option[String] = None, run_id: Option[String] = None, working_dir: Option[String] = None,
                       skip_checkpoints: Boolean = false)

