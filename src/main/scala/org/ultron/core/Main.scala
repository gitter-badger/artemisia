package org.ultron.core

import org.ultron.config.AppSetting
import scopt.OptionParser


object Main {

  var show_usage_on_error = true
  def main(args: Array[String]): Unit = {
    parseCmdLineArguments(args,show_usage_on_error) match {
      case cmd_line_params @ AppSetting(Some("run"), Some(_), _, _, _, _,_) => Command.run(cmd_line_params)
      case cmd_line_params @ AppSetting(Some("doc"), _, _, _, _, _,_) => Command.doc(cmd_line_params)
      case cmd_line_params @ _ => throw new IllegalArgumentException("--help to see supported options")
    }
  }

  private[core] def parseCmdLineArguments(args: Array[String],show_usage_on_error: Boolean = true): AppSetting = {

    val parser = new OptionParser[AppSetting]("ultron") {
      this.
      head("ultron", "0.1")
      cmd("run") action { (x, c) => c.copy(cmd = Some("run")) } children {
        opt[String]('l', "location") required() action { (x, c) => c.copy(value = Some(x)) } text "location of the job conf"
        opt[String]('d',"workdir") action { (x,c) => c.copy( working_dir = Some(x) ) } text "set the working directory for the current job"
        opt[Unit]('n',"no-checkpoint") action { (x,c) => c.copy(skip_checkpoints = true) } text "set this property skip checkpoints"
      } text "Execute Dag workflow"
      cmd("doc") action { (x, c) => c.copy(cmd = Some("doc")) } children {
        opt[String]('t', "task") action { (x, c) => c.copy(value = Some(x)) }
      } text "display Documentation"
      opt [String]("context") valueName "k1=v1,k2=v2..." action { (x, c) => c.copy(context = Some(x)) }
      opt[String]('c', "config") action { (x, c) => c.copy(config = Some(x)) } text "configuration file"
      opt[String]('w', "workflow_id") action { (x, c) => c.copy(run_id = Some(x)) } text "workflow_id for execution"

      override def showUsageOnError = show_usage_on_error

      override def errorOnUnknownArgument = true
    }
     parser.parse(args, AppSetting()).getOrElse(AppSetting())

  }
}

