package night.homework.lesson02_20180721_scala2file

import night.homework.common.io._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * Created by Night on 2018/7/21.
  */
object ScalaToFileApp {
  def main(args: Array[String]): Unit = {
    implicit def traversableSaveToFile[T : ClassTag](value: Traversable[T]) = TraversableIO(value);

    val list = ListBuffer[TestData]()
    for (i <- 1 to 100) {
      list += TestData.randomTestData
    }

    list
      .writer(mutable.HashMap[String,String]())
      .format(ReadWriteFormat.Text)
      .mode(SaveMode.Overwrite)
     // .text("D:\\data\\TestData\\test.txt")



    val readList = TraversableIO
      .read[TestData](mutable.HashMap[String,String]())
      .text("D:\\data\\TestData\\test.txt")

    readList.map(el => println(el.toString))
  }

}


