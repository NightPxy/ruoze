package night.homework.common.io.readwrite.text

import java.io.{File, FileWriter}
import java.util.Date

import night.homework.common.io._
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/22.
  */
private[io] class TextTraversableReadWriter
  extends TraversableWriteAble with TraversableReadAble with UsingPattern {
  implicit def readWriteConf(writerConf: mutable.HashMap[String, String]) = ReadWriteConf(writerConf)
  private val lineSeq = "\n";
  private val fieldSeq = "\t";

  private def write[T: ClassTag](traversableData: Traversable[T], path:String): Unit = {
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    runtimeClassFields.map(field => field.setAccessible(true))
    val itemSave= (element:T,writer:FileWriter) => {
      runtimeClassFields.map(field => {
        if (field.getGenericType == classOf[Date]) {
          writer.write(s"${simpleDateFormat.format(field.get(element).asInstanceOf[Date])}${fieldSeq}")
        }
        else {
          writer.write(s"${field.get(element).toString}${fieldSeq}")
        }
      })
      writer.write(s"${lineSeq}")
    }
    using(new FileWriter(path, true)) {
      writer => {
        traversableData.map {
          element => itemSave(element,writer)
        }
      }
    }
  }

  override def save[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]): Unit = {
    import night.homework.common.io.SaveMode
    val saveMode = conf.outputMode
    val path = conf.outputTextPath

    val file = new File(path);
    if (file.exists && !file.isFile) {
      throw new Exception(s"写入失败,[${path}] 不是一个文件路径");
    }
    val fileParent = file.getParentFile
    if (!fileParent.exists) {
      fileParent.mkdirs
    }

    saveMode match {
      case SaveMode.ErrorIfExists => if (file.exists) throw new Exception(s"文件写入失败 ${path} 已经存在")
      case SaveMode.Ignore => if (!file.exists) write(traversableData, path)
      case SaveMode.Append => write(traversableData, path)
      case SaveMode.Overwrite => if (file.exists) file.delete; write(traversableData, path)
    }
  }

  override def read[T: ClassTag](conf: mutable.HashMap[String, String]): List[T] = {
    val path = conf.inputTextPath;
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    runtimeClassFields.map(field => field.setAccessible(true))

    using(Source.fromFile(path)) {
      reader => {
        val listBuffer = ListBuffer[T]()
        for (line <- reader.getLines) {
          val element = runtimeClass.newInstance().asInstanceOf[T]
          val rows = line.split(fieldSeq)
          if (rows.length == runtimeClassFields.length) {
            for (i <- 0 until runtimeClassFields.length) {
              val field = runtimeClassFields(i);
              val value = rows(i);
              if (field.getGenericType == classOf[Date]) {
                field.set(element, simpleDateFormat.parse(value))
              }
              else {
                field.set(element, value)
              }
            }
            listBuffer += element
          }
        }
        return listBuffer.toList
      }
    }
  }

}

private[io] object TextTraversableReadWriter {
  def apply(): TextTraversableReadWriter= new TextTraversableReadWriter()
}
