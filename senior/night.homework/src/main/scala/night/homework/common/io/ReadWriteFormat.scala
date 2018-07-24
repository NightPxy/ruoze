package night.homework.common.io

import night.homework.common.io.SaveMode.Value

/**
  * Created by Night on 2018/7/22.
  */
object ReadWriteFormat extends Enumeration {
  type ReadWriteFormat = Value

  /**
    * 文本型文件
    */
  val Text = Value("Text")

  /**
    * Jdbc
    */
  val Jdbc = Value("Jdbc")
}
