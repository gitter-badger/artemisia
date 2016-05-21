package tech.artemisia.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory, ConfigObject, ConfigRenderOptions}
import tech.artemisia.config.AppContext.{CoreSetting, DagSetting, Logging}
import tech.artemisia.core.dag.Message.TaskStats
import tech.artemisia.core.{AppLogger, Keywords}
import tech.artemisia.task.Component
import tech.artemisia.util.HoconConfigUtil.Handler
import tech.artemisia.util.{FileSystemUtil, Util}
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration


/**
 *  Created by chlr on 11/28/15.
 */


class AppContext(cmd_line_param: AppSetting) {

  val skipCheckpoints = cmd_line_param.skip_checkpoints
  val globalConfigFile = Util.getGlobalConfigFileLocation()
  var payload = getConfigObject(cmd_line_param)
  val logging: Logging =  AppContext.parseLoggingFromPayload(payload.as[Config]("__setting__.logging"))
  val dagSetting: DagSetting = AppContext.parseDagSettingFromPayload(payload.as[Config]("__setting__.dag"))
  val coreSetting: CoreSetting = AppContext.parseCoreSettingFromPayload(payload.as[Config]("__setting__.core"))
  val runId: String = cmd_line_param.run_id.getOrElse(Util.getUUID)
  val workingDir: String = cmd_line_param.working_dir.getOrElse(FileSystemUtil.joinPath(payload.getString("__setting__.core.working_dir"),runId))
  val checkpoints: mutable.Map[String,TaskStats] = if (skipCheckpoints) mutable.Map() else readCheckpoint
  val componentMapper: Map[String,Component] = payload.asMap[String]("__setting__.components") map {
    case (name,component) => { (name, Class.forName(component).getConstructor().newInstance().asInstanceOf[Component] ) }
  }

  /**
   * merge all config objects (Global, Code, Context) to provide unified code config object
   * @param cmd_line_param case class of command line options
   * @return full unified config object
   */
  private[config] def getConfigObject(cmd_line_param: AppSetting): Config = {
    val empty_object = ConfigFactory.empty()
    val reference = ConfigFactory parseString FileSystemUtil.readResource("/reference.conf")
    val context = (cmd_line_param.context map ( ConfigFactory parseString _ )).getOrElse(empty_object)
    val config_file = (cmd_line_param.config map { x => Util.readConfigFile(new File(x)) }).getOrElse(empty_object)
    val code = (cmd_line_param.cmd filter { _ == "run" } map
      { x => Util.readConfigFile(new File(cmd_line_param.value.get)) }).getOrElse(empty_object)
    val global_config_option = (globalConfigFile map { x => Util.readConfigFile(new File(x)) } ).getOrElse(empty_object)
    context withFallback config_file withFallback code withFallback global_config_option withFallback reference
  }


  override def toString = {
    val options = ConfigRenderOptions.defaults() setComments false setFormatted true setOriginComments false setJson true
    payload.root().render(options)
  }


  /**
   *
   * @return checkpoint file for the session
   */
  def checkpointFile = new File(FileSystemUtil.joinPath(workingDir,Keywords.Config.CHECKPOINT_FILE))

  /**
   *
   * @return read checkpoint and return a map of [[TaskStats]]
   */
  protected[config] def readCheckpoint: mutable.Map[String,TaskStats] = {
    val checkpoints = mutable.Map[String,TaskStats]()
    if (checkpointFile.exists()) {
      AppLogger info s"checkpoint file $checkpointFile detected"
      Util.readConfigFile(checkpointFile).root() foreach {
        case (key,value: ConfigObject) => checkpoints += (key -> TaskStats(value.toConfig))
      }
    }
    checkpoints
  }

  /**
   *
   * @param task_name
   * @param task_stats
   */
  def writeCheckpoint(task_name: String, task_stats: TaskStats): Unit = {

    checkpoints += (task_name -> task_stats)
    payload = task_stats.taskOutput withFallback payload

    val content = checkpoints.foldLeft(ConfigFactory.empty()) {
      (cp_config: Config, cp: (String, TaskStats)) => {
        ConfigFactory.parseString(cp._2.toConfig(cp._1).root().render(ConfigRenderOptions.concise())) withFallback cp_config
      }
    }
    if (!this.skipCheckpoints) {
      FileSystemUtil.writeFile(content.root().render(ConfigRenderOptions.concise()), checkpointFile, append = false)
    }
  }

}


object AppContext {

  private[config] case class DagSetting(attempts: Int, concurrency: Int, heartbeat_cycle: FiniteDuration,
                                        cooldown: FiniteDuration, disable_assertions: Boolean, ignore_conditions: Boolean)
  private[config] case class CoreSetting(working_dir: String)
  private[config] case class Logging(console_trace_level: String, file_trace_level: String)

  def parseLoggingFromPayload(payload: Config) = {
    Logging(console_trace_level = payload.as[String]("console_trace_level"), file_trace_level = payload.as[String]("file_trace_level"))
  }

  def parseCoreSettingFromPayload(payload: Config) = {
    CoreSetting(working_dir = payload.as[String]("working_dir"))
  }

  def parseDagSettingFromPayload(payload: Config) = {
    DagSetting(attempts = payload.as[Int]("attempts"), concurrency = payload.as[Int]("concurrency"),
      heartbeat_cycle = payload.as[FiniteDuration]("heartbeat_cycle"), cooldown = payload.as[FiniteDuration]("cooldown"),
      disable_assertions = payload.as[Boolean]("disable_assertions"), ignore_conditions = payload.as[Boolean]("ignore_conditions"))
  }

}

case class AppSetting(cmd: Option[String] = Some("run"), value: Option[String] = None, context: Option[String] = None
                      , config: Option[String] = None, run_id: Option[String] = None, working_dir: Option[String] = None,
                      skip_checkpoints: Boolean = false)
