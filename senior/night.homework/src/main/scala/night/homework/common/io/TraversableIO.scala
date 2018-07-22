package night.homework.common.io

import java.io.{File, FileWriter}

import night.homework.common.io.FileSaveMode.FileSaveMode
import night.homework.common.io.writer.TraversableWriter
import night.homework.common.utils.patterns.UsingPattern

import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/21.
  */
case class TraversableIO[T <: TraversableWriteAble: ClassTag](traversableData: Traversable[T]) extends UsingPattern {

//  private val rowSeq = "\n";
//
//  private def writeFile(path: String) = using(new FileWriter(path, true)) {
//    writer => {
//      traversableData.map {
//        ele => writer.write(s"${ele.writeLine}${rowSeq}")
//      }
//    }
//  }
//
//  /**********************************   Public   ****************************************/

  def writer[T <: TraversableWriteAble: ClassTag]() = TraversableWriter(traversableData)

//  def saveToFile(path: String, mode: FileSaveMode = FileSaveMode.Overwrite): Unit = {
//    val file = new File(path);
//    if (file.exists && !file.isFile) {
//      throw new Exception(s"写入失败,[${path}] 不是一个文件路径");
//    }
//    val fileParent = file.getParentFile
//    if (!fileParent.exists) {
//      fileParent.mkdirs
//    }
//
//    mode match {
//      case FileSaveMode.ErrorIfExists => if (file.exists) throw new Exception(s"文件写入失败 ${path} 已经存在")
//      case FileSaveMode.Ignore => if (!file.exists) writeFile(path)
//      case FileSaveMode.Append => writeFile(path)
//      case FileSaveMode.Overwrite => if (!file.exists) file.delete; writeFile(path)
//    }
//  }
}






//