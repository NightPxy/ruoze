package night.homework.common.io.readwrite

import night.homework.common.io.readwrite.text.TextTraversableReadWriter
import night.homework.common.io.{ReadWriteConf, ReadWriteFormat, TraversableReadAble}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/23.
  */
class TraversableReader[T:ClassTag](conf: mutable.HashMap[String, String]) {

  implicit def readWriteConf(readConf: mutable.HashMap[String, String]) = ReadWriteConf(readConf)

  private def traversableReadAble():TraversableReadAble = conf.inputFormat match {
    case ReadWriteFormat.Text => TextTraversableReadWriter()
    case _=> throw  new Throwable("未受支持的 fileFormat 类型");
  }

  def text():ListBuffer[T] =  traversableReadAble.read(this.conf)

  def text(path:String):ListBuffer[T] = {
    conf.inputTextPath(path)
    text()
  }
}
object TraversableReader {
  def apply[T:ClassTag](conf: mutable.HashMap[String, String]): TraversableReader[T] =
    new TraversableReader[T](conf: mutable.HashMap[String, String])
}
