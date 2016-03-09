package org.ultron.task.localhost

import net.ceedubs.ficus.Ficus._
import org.ultron.TestSpec

/**
 * Created by chlr on 3/6/16.
 */
class ScriptTaskSpec extends TestSpec {

  "ScriptTask" must "must execute command and parse result" in {
    val value = "foo" -> "bar"
    val task = new ScriptTask(script = s"echo { ${value._1} = ${value._2} }",parseOutput = true)
    val result = task.execute
    result.as[String](value._1) must be (value._2)
  }

  it must "throw an exception when script fails" in {
    val exception = intercept[AssertionError] {
      val value = "foo" -> "bar"
      val task = new ScriptTask(script = s"echo1 { ${value._1} = ${value._2} }", parseOutput = false)
      task.execute
    }
    exception.getMessage must be ("assertion failed: Non Zero return code detected")
  }

  it must "apply environment variables correctly"  in {
    val env = Map("foo" -> "bar")
    val task = new ScriptTask(script = s"echo { key = $${foo} }", env = env, parseOutput = true)
    val result = task.execute
    result.as[String]("key") must be(env("foo"))
  }

  it must "execute multi-line script correctly" in {
    val cmd =
      """
        |echo {
        |echo foo = bar
        |echo }
      """.stripMargin
    val task = new ScriptTask(script = cmd,parseOutput = true)
    val result = task.execute
    result.as[String]("foo") must be ("bar")
  }

}
