package night.homework.common.io


import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/22.
  */
private[io] trait TraversableReadWriter {
  protected var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]")
  protected var simpleDateFormat: SimpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")

}

private[io] trait TraversableWriteAble extends TraversableReadWriter {
  def save[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]);
}

private[io] trait TraversableReadAble extends TraversableReadWriter {
  def read[T: ClassTag](conf: mutable.HashMap[String, String]): ListBuffer[T]
}
