package night.homework.common.io

/**
  * Created by Night on 2018/7/22.
  */
object FileSaveMode extends Enumeration {
  type FileSaveMode = Value
  /**
    * 写入文件(如果文件已存在,将追加写入)
    */
  val Append = Value(0, "Append")
  /**
    * 写入文件(如果文件已存在,将覆盖全部数据再写入)
    * 注意:
    * Overwrite 将以删除目标文件再写入的方式进行.
    * 此过程不保证原子性,即可能存在文件被删除后无法写入导致数据丢失的可能
    */
  val Overwrite = Value(1, "Overwrite")
  /**
    * 写入文件(如果文件已存在,将抛出异常)
    */
  val ErrorIfExists = Value(2, "ErrorIfExists")
  /**
    * 写入文件(如果文件已存在,将忽略本次写入)
    */
  val Ignore = Value(3, "Ignore")
}
