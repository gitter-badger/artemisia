package org.ultron.util

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.matching.Regex

/**
 * Created by chlr on 3/18/16.
 */
object HoconConfigUtil {

  implicit class Handler(val config: Config) {

    def as[T: ConfigReader](key: String): T = {
      implicitly[ConfigReader[T]].read(config, key)
    }

    def asMap[T: ConfigReader](key: String): Map[String,T] = {
      val configObject = config.getConfig(key).root()
      val result = configObject.keySet().asScala map { x => x -> configObject.toConfig.as[T](x) }
      result.toMap
    }

    def getAs[T: ConfigReader](key: String): Option[T] = {
      if (config.hasPath(key)) Some(as[T](key)) else None
    }

  }

  implicit def hardResolve(root: Config): Config = {

    def resolve(config: Config): Config = {

      val processed = for (key <- config.root().keySet().asScala) yield {
        config.getAnyRef(key) match {
          case x: String => key -> resolveString(x)
          case x: java.lang.Iterable[AnyRef] => key -> resolveList(config.getAnyRefList(key).asScala.toIterable)
          case x: java.util.Map[String, AnyRef] => key -> resolve(config.getConfig(key)).root().unwrapped()
          case x => key -> x
        }
      }
      ConfigFactory parseMap processed.toMap.asJava
    }

    def resolveString(str: String) = {
      val tempNode = ConfigFactory parseString s"""{ __temp__ = $str }"""
      tempNode.resolveWith(root).getString("__temp__")
    }

    def resolveList(list: Iterable[Any]): java.lang.Iterable[Any] = {
      val processed: Iterable[Any] = for (node <- list) yield {
        node match {
          case x: java.util.Map[String,AnyRef] => {
            resolve(ConfigValueFactory.fromMap(x).toConfig).root().unwrapped()
          }
          case x: java.lang.Iterable[AnyRef] => resolveList(x.asScala)
          case x: String => resolveString(x)
          case x => x
        }
      }
      processed.asJava
    }
    resolve(root)
  }

  implicit def configToConfigResolver(config: Config): ConfigResolver = {
    new ConfigResolver(config)
  }

  class ConfigResolver(val root: Config)  {

    val hardResolve = resolveConfig(root)

    private def resolveConfig(config: Config): Config = {
      val processed = for (key <- config.root().keySet().asScala) yield {
        config.getAnyRef(key) match {
          case x: String => key -> resolveString(x)
          case x: java.lang.Iterable[AnyRef] => key -> resolveList(config.getAnyRefList(key).asScala.toIterable)
          case x: java.util.Map[String, AnyRef] => key -> resolveConfig(config.getConfig(key)).root().unwrapped()
          case x => key -> x
        }
      }
      ConfigFactory parseMap processed.toMap.asJava
    }


    private def resolveString(str: String) = {
      def replacement(m: Regex.Match) = {
        import java.util.regex.Matcher
        require(m.groupCount == 1)
        Matcher.quoteReplacement(root.getString(m group 1))
      }
      """\$\{[\w]+\}""".r.replaceAllIn(str, replacement _)
    }

    private def resolveList(list: Iterable[Any]): java.lang.Iterable[Any] = {
      val processed: Iterable[Any] = for (node <- list) yield {
        node match {
          case x: java.util.Map[String,AnyRef] => {
            resolveConfig(ConfigValueFactory.fromMap(x).toConfig).root().unwrapped()
          }
          case x: java.lang.Iterable[AnyRef] => resolveList(x.asScala)
          case x: String => resolveString(x)
          case x => x
        }
      }
      processed.asJava
    }

  }



  trait ConfigReader[T] {
    def read(config: Config, path: String): T
  }

  implicit val anyRefReader = new ConfigReader[AnyRef] {
    override def read(config: Config, path: String): AnyRef = {
      config.getAnyRef(path)
    }
  }

  implicit val anyRefListReader = new ConfigReader[List[AnyRef]] {
    override def read(config: Config, path: String): List[AnyRef] = {
      ( config.getAnyRefList(path).asScala map { _.asInstanceOf[AnyRef] } ).toList
    }
  }


  implicit val booleanReader = new ConfigReader[Boolean] {
    override def read(config: Config, path: String): Boolean = {
      config.getBoolean(path)
    }
  }


  implicit val booleanListReader = new ConfigReader[List[Boolean]] {
    override def read(config: Config, path: String): List[Boolean] = {
      config.getBooleanList(path).asScala.toList map { _.booleanValue() }
    }
  }


  implicit val configReader = new ConfigReader[Config] {
    override def read(config: Config, path: String): Config = {
      config.getConfig(path)
    }
  }


  implicit val configListReader = new ConfigReader[List[Config]] {
    override def read(config: Config, path: String): List[Config] = {
      config.getConfigList(path).asScala.toList
    }
  }


  implicit val doubleReader = new ConfigReader[Double] {
    override def read(config: Config, path: String): Double = {
      config.getDouble(path)
    }
  }


  implicit val doubleListReader = new ConfigReader[List[Double]] {
    override def read(config: Config, path: String): List[Double] = {
      config.getDoubleList(path).asScala.toList map {_.toDouble }
    }
  }

  implicit val durationReader = new ConfigReader[FiniteDuration] {
    override def read(config: Config, path: String): FiniteDuration = {
      Duration.fromNanos(config.getDuration(path).toNanos)
    }
  }

  implicit val durationListReader = new ConfigReader[List[FiniteDuration]] {
    override def read(config: Config, path: String): List[FiniteDuration] = {
      config.getDurationList(path).asScala.toList map { x => Duration.fromNanos(x.toNanos) }
    }
  }

  implicit val intReader = new ConfigReader[Int] {
    override def read(config: Config, path: String): Int = {
      config.getInt(path)
    }
  }

  implicit val intListReader = new ConfigReader[List[Int]] {
    override def read(config: Config, path: String): List[Int] = {
      config.getIntList(path).asScala.toList map { _.toInt }
    }
  }

  implicit val longReader = new ConfigReader[Long] {
    override def read(config: Config, path: String): Long = {
      config.getLong(path)
    }
  }

    implicit val longListReader = new ConfigReader[List[Long]] {
    override def read(config: Config, path: String): List[Long] = {
      config.getLongList(path).asScala.toList map { _.toLong }
    }
  }

  implicit val charReader = new ConfigReader[Char] {
    override def read(config: Config, path: String): Char = {
      val data = config.getString(path)
      require(data.length == 1, "Character length is not 1")
      data.toCharArray.apply(0)
    }
  }

  implicit val stringReader = new ConfigReader[String] {
    override def read(config: Config, path: String): String = {
      config.getString(path)
    }
  }

  implicit val stringListReader = new ConfigReader[List[String]] {
    override def read(config: Config, path: String): List[String] = {
      config.getStringList(path).asScala.toList
    }
  }

}


