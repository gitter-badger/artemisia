package org.ultron

import org.scalatest._
import org.ultron.core._


/**
 * Created by chlr on 1/2/16.
 */

abstract class TestSpec extends FlatSpec with MustMatchers with PrePostTestSetup with OneInstancePerTest


trait PrePostTestSetup extends BeforeAndAfterEach {

  self: Suite =>

  val testEnv = TestEnv
  env = testEnv

  /**
   * any pre-test code setup goes here
   */
  abstract override def beforeEach(): Unit = {
    env = testEnv
  }

  /**
   * Any post test clean code goes here
   */
  abstract override def afterEach(): Unit = {

  }

}