package night.homework.common.io.text

import java.io.{File, FileWriter}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

import night.homework.common.io.{FileSaveMode, TraversableReadAble, TraversableReadWriter, TraversableWriteAble}
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/22.
  */
class TextTraversableReadWriter[T: ClassTag]
  extends TraversableWriteAble with TraversableReadAble with UsingPattern {

  import night.homework.common.utils.impros.all._;


  private def write(traversableData: Traversable[T], conf: mutable.HashMap[String, String]): Unit = {
    val path = conf.assertGet("output.path");
    val lineSeq = conf.assertGet("output.line.seq");
    val fieldSeq = conf.assertGet("output.field.seq");
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields

    using(new FileWriter(path, true)) {
      writer => {
        traversableData.map {
          ele => {
            runtimeClassFields.map(field => {
              field.setAccessible(true);
              if (field.getGenericType == classOf[Date]) {
                writer.write(s"${simpleDateFormat.format(field.get(ele).asInstanceOf[Date])}${fieldSeq}")
              }
              else {
                writer.write(s"${field.get(ele).toString}${fieldSeq}")
              }
            })
            writer.write(s"${lineSeq}")
          }
        }
      }
    }
  }

  override def save[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]): Unit = {
    import night.homework.common.io.FileSaveMode
    val saveMode = FileSaveMode.withName(conf.assertGet("output.saveMode"))
    val path = conf.assertGet("output.path");

    val file = new File(path);
    if (file.exists && !file.isFile) {
      throw new Exception(s"写入失败,[${path}] 不是一个文件路径");
    }
    val fileParent = file.getParentFile
    if (!fileParent.exists) {
      fileParent.mkdirs
    }

    saveMode match {
      case FileSaveMode.ErrorIfExists => if (file.exists) throw new Exception(s"文件写入失败 ${path} 已经存在")
      case FileSaveMode.Ignore => if (!file.exists) write(traversableData, conf)
      case FileSaveMode.Append => write(traversableData, conf)
      case FileSaveMode.Overwrite => if (file.exists) file.delete; write(traversableData, conf)
    }
  }

  override def read[T: ClassTag](conf: mutable.HashMap[String, String]): ListBuffer[T] = {
    val path = conf.assertGet("intput.path");
    val lineSeq = conf.assertGet("intput.line.seq");
    val fieldSeq = conf.assertGet("intput.field.seq");
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields

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
              field.setAccessible(true);
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
        return listBuffer
      }
    }
  }

}

object TextTraversableReadWriter {
  def apply[T: ClassTag](): TextTraversableReadWriter[T]= new TextTraversableReadWriter[T]()
}
