package night.homework.common.io

import night.homework.common.io.readwrite.{TraversableReader, TraversableWriter}
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/21.
  */
case class TraversableIO[T : ClassTag](traversableData: Traversable[T]) extends UsingPattern {

  def writer[T : ClassTag](conf:mutable.HashMap[String,String]) = TraversableWriter(traversableData,conf)
  def writer[T : ClassTag]() = TraversableWriter(traversableData,mutable.HashMap[String,String]())
}

object TraversableIO {
  def read[T : ClassTag](conf:mutable.HashMap[String,String]) = TraversableReader(conf)
  def read[T : ClassTag]() = TraversableReader(mutable.HashMap[String,String]())
}