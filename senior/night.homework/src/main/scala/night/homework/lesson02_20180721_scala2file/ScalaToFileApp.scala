package night.homework.lesson02_20180721_scala2file

import java.text.SimpleDateFormat
import java.util
import java.util.{Calendar, Date, UUID}

import night.homework.common.io.FileSaveMode.FileSaveMode
import night.homework.common.io.{FileFormat, FileSaveMode, TraversableIO, TraversableWriteAble}
import night.homework.common.utils.RandomUtils

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/21.
  */
object ScalaToFileApp {
  def main(args: Array[String]): Unit = {
    implicit def traversableSaveToFile[T <: TraversableWriteAble : ClassTag](value: Traversable[T]) = TraversableIO(value);

    val list = ListBuffer[TestData]()
    for (i <- 1 to 1000000) {
      list += TestData.randomTestData
    }

    val a = List(List(1,2),List(3,4));

    println(1/0)


//    list
    //      .writer.format(FileFormat.Text).mode(FileSaveMode.Overwrite).save("D:\\data\\TestData\\test.txt")
  }

}


