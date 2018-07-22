package night.homework.common.io.writer

import java.io.{File, FileWriter}

import night.homework.common.io.FileFormat.FileFormat
import night.homework.common.io.{FileFormat, FileSaveMode, TraversableReadWriter, TraversableWriteAble}
import night.homework.common.io.FileSaveMode.FileSaveMode
import night.homework.common.io.text.TextTraversableReadWriter
import night.homework.common.utils.patterns.UsingPattern

import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/22.
  */
case class TraversableWriter [T <: TraversableWriteAble: ClassTag](traversableData: Traversable[T]) extends UsingPattern {

  private var lineSeq = "\n";
  private var fileFormat = FileFormat.Text;
  private var saveMode= FileSaveMode.Overwrite;
  private def traversableReadWriter():TraversableReadWriter = this.fileFormat match {
    case FileFormat.Text => TextTraversableReadWriter()
    case _=> throw  new Throwable("未受支持的 fileFormat 类型");
  }

  /**********************************   Public   ****************************************/

  def lineSeparator(seq: String) = {
    this.lineSeq = seq
    this
  }

  def  format(chooseFileFormat: FileFormat) =  {
    this.fileFormat = chooseFileFormat;
    this;
  }

  def mode( chooseMode: FileSaveMode) = {
    this.saveMode = chooseMode
    this
  }

  def text(path:String) = {
    this.format(FileFormat.Text)
    this.save(path)
    this
  }

  def save(path:String) = {
    val file = new File(path);
    if (file.exists && !file.isFile) {
      throw new Exception(s"写入失败,[${path}] 不是一个文件路径");
    }
    val fileParent = file.getParentFile
    if (!fileParent.exists) {
      fileParent.mkdirs
    }

    this.saveMode match {
      case FileSaveMode.ErrorIfExists => if (file.exists) throw new Exception(s"文件写入失败 ${path} 已经存在")
      case FileSaveMode.Ignore => if (!file.exists) traversableReadWriter.write(traversableData,path,this.lineSeq)
      case FileSaveMode.Append => traversableReadWriter.write(traversableData,path,this.lineSeq)
      case FileSaveMode.Overwrite => if (!file.exists) file.delete; traversableReadWriter.write(traversableData,path,this.lineSeq)
    }

    this
  }
}
