package night.homework.common.io.readwrite

import night.homework.common.io.ReadWriteFormat.ReadWriteFormat
import night.homework.common.io.readwrite.jdbc.JdbcTraversableReadWriter
import night.homework.common.io.readwrite.text.TextTraversableReadWriter
import night.homework.common.io.{ReadWriteConf, ReadWriteFormat, TraversableReadAble}
import night.homework.common.scalikejdbc.ScalikejdbcConf

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/23.
  */
class TraversableReader(conf: mutable.HashMap[String, String]) {

  implicit def readWriteConf(readConf: mutable.HashMap[String, String]) = ReadWriteConf(readConf)
  implicit def jdbcConf(readConf: mutable.HashMap[String, String]) = ScalikejdbcConf(readConf)

  private def traversableReadAble():TraversableReadAble = conf.inputFormat match {
    case ReadWriteFormat.Text => TextTraversableReadWriter()
    case ReadWriteFormat.Jdbc => JdbcTraversableReadWriter()
    case _=> throw  new Throwable("未受支持的 fileFormat 类型");
  }

  private def doRead[T:ClassTag]():List[T] = traversableReadAble.read(this.conf);

  def text[T:ClassTag]():List[T] = doRead[T]

  def text[T:ClassTag](path:String):List[T] = {
    conf.inputTextPath(path)
    text()
  }

  def jdbc[T:ClassTag](table:String, driver:String,url:String,user:String,password:String):List[T] = {
    conf.inputFormat(ReadWriteFormat.Jdbc).inputJdbcTable(table)
    conf.driver(driver).url(url).user(user).password(password)
    doRead[T]
  }
}
object TraversableReader {
  def apply[T:ClassTag](conf: mutable.HashMap[String, String]): TraversableReader =
    new TraversableReader(conf: mutable.HashMap[String, String])
}
