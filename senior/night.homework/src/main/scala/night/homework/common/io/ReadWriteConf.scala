package night.homework.common.io

import night.homework.common.io.ReadWriteFormat.ReadWriteFormat
import night.homework.common.io.SaveMode.SaveMode

import scala.collection.mutable

/**
  * Created by Night on 2018/7/23.
  */
case class ReadWriteConf(hashMap: mutable.HashMap[String, String]) {

  import night.homework.common.utils.impros.all._

  def outputFormat(format: ReadWriteFormat) = hashMap.set("output.format", ReadWriteFormat(format.id).toString)

  def outputFormat() = ReadWriteFormat.withName(hashMap.getOrElse("output.format", "Text"))

  def outputMode(saveMode: SaveMode) = hashMap.set("output.mode", SaveMode(saveMode.id).toString)

  def outputMode() = SaveMode.withName(hashMap.getOrElse("output.mode", "ErrorIfExists"))

  def outputTextPath(path: String) = hashMap.set("output.text.path", path)

  def outputTextPath() = hashMap.assertGet("output.text.path")

  def inputTextPath(path: String) = hashMap.set("input.text.path", path)
  def inputTextPath() = hashMap.assertGet("input.text.path")

  def inputFormat(format: ReadWriteFormat) = hashMap.set("input.format", ReadWriteFormat(format.id).toString)

  def inputFormat() = ReadWriteFormat.withName(hashMap.getOrElse("input.format", "Text"))
}
