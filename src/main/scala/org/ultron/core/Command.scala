package org.ultron.core

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import org.slf4j.LoggerFactory
import org.ultron.config.{AppContext, AppSetting, Keywords}
import org.ultron.core.dag.{ActorSysManager, Dag}

/**
 * Created by chlr on 12/30/15.
 */
object Command {

  private def prepareAppContext(cmd_line_params: AppSetting) = {
    val app_context = new AppContext(cmd_line_params)
    configureLogging(app_context)
    AppLogger debug s"workflow_id: ${app_context.run_id}"
    AppLogger debug s"working directory: ${app_context.working_dir}"
    if (app_context.global_config_file.nonEmpty) {
      AppLogger debug s"global config file: ${app_context.global_config_file.get}"
    }
    app_context
  }


  private def configureLogging(app_context: AppContext) = {
      val context = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
      val jc = new JoranConfigurator
      jc.setContext(context)
      context.reset()
      context.putProperty("log.console.level", app_context.logging.console_trace_level)
      context.putProperty("log.file.level", app_context.logging.file_trace_level)
      context.putProperty("env.working_dir", app_context.core_setting.working_dir)
      context.putProperty("workflow_id", app_context.run_id)
      jc.doConfigure(this.getClass.getResource("/logback_config.xml").getFile)
  }


  def run(cmd_line_params: AppSetting) = {
    AppLogger info "request for run command acknowledged"
    val app_context = prepareAppContext(cmd_line_params)
    AppLogger debug "context object created"
    val dag = Dag(app_context)
    AppLogger debug "starting Actor System"
    val actor_sys_manager =  new ActorSysManager(app_context)
    val workers = actor_sys_manager.createWorker(Keywords.ActorSys.CUSTOM_DISPATCHER)
    val dag_player = actor_sys_manager.createPlayer(dag,workers)
    dag_player ! 'Play
  }

  def doc(cmd_line_params: AppSetting) = {
  }

}
