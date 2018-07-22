package night.homework.common.io


import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/22.
  */
abstract class TraversableReadWriter[T : ClassTag] {
  protected val runtimeClass = classTag[T].runtimeClass.getClass
  protected val runtimeClassFields = runtimeClass.getDeclaredFields


  def write[T <: TraversableWriteAble: ClassTag](traversableData: Traversable[T], path: String, lineSeq:String,fieldSeq:String = "\t")
  def read[T <: TraversableReadAble: ClassTag](traversableData: Traversable[T], path: String, lineSeq:String,fieldSeq:String = "\t")
}

trait TraversableWriteAble {
  def writeLine() : String
}
trait TraversableReadAble {
  def readLine[T](line:String) : T
}
