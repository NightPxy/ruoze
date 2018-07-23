package night.homework.common.io.reader

import night.homework.common.io.text.TextTraversableReadWriter

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/23.
  */
class TraversableReader[T:ClassTag] {
  private var lineSeq = "\n";
  private var fieldSeq: String = "\t"
  private var conf: mutable.HashMap[String, String] = _;
  def setConf(conf:mutable.HashMap[String, String]) = {
    this.conf = conf;
  }

  def text(path:String):ListBuffer[T] =  TextTraversableReadWriter().read()
}
object TraversableReader {
  def apply[T:ClassTag](): TraversableReader[T] = new TraversableReader[T]()
}
