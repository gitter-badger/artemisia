package org.ultron.util

import java.io.FileNotFoundException

import org.ultron.TestSpec
import org.ultron.config.Keywords
import org.ultron.core.wire
import scaldi.Injectable._

/**
 * Created by chlr on 1/1/16.
 */

class UtilSpec extends TestSpec {
  val os_util: OSUtilTestImpl = inject[OSUtil].asInstanceOf[OSUtilTestImpl]
  override def beforeEach() = {

  }

  "The Util.readConfigFile" must "throw FileNotFoundException on non-existent file" in {
    intercept[FileNotFoundException] {
      Util.readConfigFile("Some_Non_Existant_File.conf")
    }
  }

  "Util.getGlobalConfigFileLocation" must s"must provide default value when is ${Keywords.Config.GLOBAL_FILE_REF_VAR} not set" in {
    os_util.withSysVar(Map()) {
      val default_value = this.getClass.getResource("/code/code_with_simple_mysql_component.conf").getFile
      Util.getGlobalConfigFileLocation(default_value).get must equal(default_value)
    }
  }

  it must s"must give a None if ${Keywords.Config.GLOBAL_FILE_REF_VAR} not set and the default file doesn't exist" in {
    os_util.withSysVar(Map()) {
      val default_value = "A_Non_Existant_File.conf"
      Util.getGlobalConfigFileLocation(default_value) must equal(None)
    }
  }

}
