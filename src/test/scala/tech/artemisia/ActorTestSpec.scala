package tech.artemisia

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._


/**
 * Created by chlr on 1/2/16.
 */

abstract class ActorTestSpec extends TestKit(ActorSystem("TestSystem")) with ImplicitSender with FlatSpecLike with MustMatchers with BeforeAndAfterAll with BeforeAndAfterEach {

  override def afterAll() = {
    system.shutdown()
  }

}
