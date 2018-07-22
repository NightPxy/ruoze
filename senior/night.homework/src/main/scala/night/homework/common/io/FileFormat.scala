package night.homework.common.io

import night.homework.common.io.FileSaveMode.Value

/**
  * Created by Night on 2018/7/22.
  */
object FileFormat extends Enumeration {
  type FileFormat = Value

  /**
    * 文本型文件
    */
  val Text = Value(0, "Text")
}
