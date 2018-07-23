package night.homework.common.io.writer

import java.io.{File, FileWriter}

import night.homework.common.io.FileFormat.FileFormat
import night.homework.common.io.{FileFormat, FileSaveMode, TraversableReadWriter, TraversableWriteAble}
import night.homework.common.io.FileSaveMode.FileSaveMode
import night.homework.common.io.text.TextTraversableReadWriter
import night.homework.common.utils.patterns.UsingPattern

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/22.
  */
case class TraversableWriter [T : ClassTag](traversableData: Traversable[T],conf:mutable.HashMap[String,String])
  extends UsingPattern {

  private var fileFormat = FileFormat.withName(conf.getOrElse("output.format","Text"));
  private def traversableWriter():TraversableReadWriter = this.fileFormat match {
    case FileFormat.Text => TextTraversableReadWriter[T]()
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

  def save(path:String) =  {
    traversableReadWriter.save()
    this;
  }
}
