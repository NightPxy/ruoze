package night.homework.common.io

import java.io.{File, FileWriter}

import night.homework.common.io.FileSaveMode.FileSaveMode
import night.homework.common.io.reader.TraversableReader
import night.homework.common.io.writer.TraversableWriter
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/21.
  */
case class TraversableIO[T : ClassTag](traversableData: Traversable[T]) extends UsingPattern {

  def writer[T <: TraversableWriteAble: ClassTag](conf:mutable.HashMap[String,String]) = TraversableWriter(traversableData,conf)

}

object TraversableIO {
  def read[T <:TraversableReadAble : ClassTag]() = TraversableReader()
}