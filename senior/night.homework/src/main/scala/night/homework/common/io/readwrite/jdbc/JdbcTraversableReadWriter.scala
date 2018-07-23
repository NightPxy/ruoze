package night.homework.common.io.readwrite.jdbc

import night.homework.common.io.{ReadWriteConf, TraversableReadAble, TraversableWriteAble}
import night.homework.common.scalikejdbc.ScalikejdbcConf
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/23.
  */
private[io] class JdbcTraversableReadWriter
  extends TraversableWriteAble with TraversableReadAble with UsingPattern{
  implicit def readWriteConf(conf: mutable.HashMap[String, String]) = ReadWriteConf(conf)
  implicit def jdbcConf(conf: mutable.HashMap[String, String]) = ScalikejdbcConf(conf)

  override def save[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]): Unit = {
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    runtimeClassFields.map(field => field.setAccessible(true))


  }

  override def read[T: ClassTag](conf: mutable.HashMap[String, String]): ListBuffer[T] = ???
}
