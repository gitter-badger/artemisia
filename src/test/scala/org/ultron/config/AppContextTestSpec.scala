package org.ultron.config

import java.io.{File, FileNotFoundException}

import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import org.ultron.util.HoconConfigUtil.Handler
import org.ultron.TestSpec
import org.ultron.core.dag.Message.TaskStats
import org.ultron.core.{Keywords, wire}
import org.ultron.util.{FileSystemUtil, OSUtil, OSUtilTestImpl}
import scaldi.Injectable._

/**
*  Created by chlr on 12/4/15.
*/
class AppContextTestSpec extends TestSpec {

  var cmd_line_params: AppSetting = _
  var os_util: OSUtilTestImpl = _
  var sys_var:(String,String) = _
  var app_context: AppContext = _

  override def beforeEach(): Unit = {
    os_util  = inject[OSUtil].asInstanceOf[OSUtilTestImpl]
    sys_var = Keywords.Config.GLOBAL_FILE_REF_VAR -> this.getClass.getResource("/global_config.conf").getFile
    cmd_line_params = AppContextTestSpec.defualtTestCmdLineParams
  }

  "The Config Object" must s"Read the Global File and merge it with default config file" in {

    os_util.withSysVar(Map(sys_var)) {
      app_context = new AppContext(cmd_line_params)
      app_context.payload = app_context.payload.resolve()
      info("checking if job_config is in effect")
      app_context.payload.as[String]("dummy_step1.config.table") must be ("dummy_table")
      info("checking if global_config is in effect")
      app_context.payload.as[String]("dummy_step1.config.dsn") must be ("mysql_database")
      info("checking if code config variable resolution")
      app_context.payload.as[Int]("dummy_step1.config.misc_param") must be (100)
      info("checking if reference config is available")
      app_context.payload.as[String]("foo") must be("bar")
    }
  }

  it must "throw an FileNotFoundException when the GLOBAL File doesn't exists" in  {

     sys_var = Keywords.Config.GLOBAL_FILE_REF_VAR  ->
      (this.getClass.getResource("/global_config.conf").getFile+"_not_exists") // refering to non-existant file

    os_util.withSysVar(Map(sys_var)) {
      info("intercepting exception")
      val ex = intercept[FileNotFoundException] {
        app_context = new AppContext(cmd_line_params)
      }
      info("validating exception message")
      ex.getMessage must be(s"The Config file ${sys_var._2} is missing")
    }
  }

  it must "throw an FileNotFoundException when the config file doesn't exist" in  {

    os_util.withSysVar(Map(sys_var)) {
      val config_file = "/not_exists_file"
      cmd_line_params = cmd_line_params.copy(config = Some(config_file))
      info("intercepting exception")
      val ex = intercept[FileNotFoundException] {
        app_context = new AppContext(cmd_line_params)
      }
      info("validating exception message")
      ex.getMessage must be(s"The Config file $config_file is missing")
    }

  }

  it must "throw a ConfigException.Parse exception on invalid context string" in {
    cmd_line_params = cmd_line_params.copy(context = Some("a==b==c"))
    info("intercepting exception")
    intercept[ConfigException.Parse] {
      app_context = new AppContext(cmd_line_params)
    }
  }

  it must "write the checkpoint in the right file with right content" in {

    val task_name = "dummy_task"
    val test_working_dir = this.getClass.getResource("/test_working_dir/appcontext/slot1").getFile
    val cmd = cmd_line_params.copy(working_dir = Some(test_working_dir))
    app_context = new AppContext(cmd)
    app_context.writeCheckpoint(task_name,AppContextTestSpec.getTaskStatsConfigObject)
    val checkpoint = ConfigFactory.parseFile(new File(FileSystemUtil.joinPath(test_working_dir,"checkpoint.conf")))
    info("validating end_time")
    checkpoint.getString(s"$task_name.${Keywords.TaskStats.END_TIME}") must be ("2016-01-18 22:27:52")
    info("validating start_time")
    checkpoint.getString(s"$task_name.${Keywords.TaskStats.START_TIME}") must be ("2016-01-18 22:27:51")

  }

  it must "read a checkpoint file " in {

    val task_name = "dummy_task"
    val test_working_dir = this.getClass.getResource("/test_working_dir/appcontext/slot2").getFile
    val cmd = cmd_line_params.copy(working_dir = Some(test_working_dir))
    app_context = new AppContext(cmd)
    val checkpoints = app_context.readCheckpoint
    val task_stats = checkpoints(task_name)
    info("validating end_time")
    task_stats.endTime must be ("2016-01-18 22:27:52")
    info("validating start_time")
    task_stats.startTime must be ("2016-01-18 22:27:51")
  }


}



object AppContextTestSpec {

  def getTaskStatsConfigObject = {
    val task_stat_config: Config = ConfigFactory parseString s"""
        |{
        |    ${Keywords.TaskStats.ATTEMPT}: 1,
        |    ${Keywords.TaskStats.END_TIME}: "2016-01-18 22:27:52",
        |    ${Keywords.TaskStats.START_TIME}: "2016-01-18 22:27:51",
        |    ${Keywords.TaskStats.STATUS}: "SUCCEEDED",
        |    ${Keywords.TaskStats.TASK_OUTPUT}: {"new_variable": 1000 }
        |}
      """.stripMargin

    TaskStats(task_stat_config)
  }


  def defualtTestCmdLineParams = {

    val job_config = Some(this.getClass.getResource("/job_config.conf").getFile)
    val code = Some(this.getClass.getResource("/code/code_with_simple_mysql_component.conf").getFile)
    val context = Some("ignore_failure=yes")
    val working_dir = None
    val cmd_line_params = AppSetting(cmd=Some("run"), value=code, context = context, config = job_config,
      working_dir = working_dir)
    cmd_line_params

  }


}
