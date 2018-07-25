package night.homework.lesson02_20180721_scalaio

import night.homework.common.io.{ReadWriteFormat, SaveMode, TraversableIO}
import night.homework.common.utils.TestUtils

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/24.
  */
object ScalaIOTest extends App with TestUtils{
  implicit def traversableSaveToFile[T: ClassTag](value: Traversable[T]) = TraversableIO(value);
  def generateData(count:Int) = {
    val list = ListBuffer[Data]()
    for (i <- 1 to count) {
      list += Data.randomTestData
    }
    list
  }




  /***************************** Text ***********************************/
  val textOutputPath = "D:\\data\\TestData\\test.txt";
  //断言 Overwrite 数据100条
  assertTrue {
    generateData(100).writer.format(ReadWriteFormat.Text).mode(SaveMode.Overwrite).text(textOutputPath)
    TraversableIO.read.text[Data](textOutputPath).length == 100
  }



  //断言text数据100条(断言 ErrorIfExists,Ignore 模式无效)
  assertTrue {
    //断言 ErrorIfExists 模式抛出异常
    assertException{
      generateData(100).writer.format(ReadWriteFormat.Text).mode(SaveMode.ErrorIfExists).text(textOutputPath)
    }
    // Ignore模式 追加100条
    generateData(100).writer.format(ReadWriteFormat.Text).mode(SaveMode.Ignore).text(textOutputPath)

    TraversableIO.read.text[Data](textOutputPath).length == 100
  }

  //断言 Overwrite 数据100条
  assertTrue {
    generateData(100).writer.format(ReadWriteFormat.Text).mode(SaveMode.Overwrite).text(textOutputPath)
    TraversableIO.read.text[Data](textOutputPath).length == 100
  }

  /***************************** Text ***********************************/
  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false"
  val user = "root"
  val password = "12abAB"
  val table = "traversableIO"
  //断言 Overwrite 数据100条
  assertTrue {
    generateData(100).writer.mode(SaveMode.Overwrite).jdbc(table,driver,url,user,password)
    TraversableIO.read.jdbc[Data](table,driver,url,user,password).length == 100
  }
  //断言text数据100条(断言 ErrorIfExists,Ignore 模式无效)
  assertTrue {
    //断言 ErrorIfExists 模式抛出异常
    assertException{
      generateData(100).writer.mode(SaveMode.ErrorIfExists).jdbc(table,driver,url,user,password)
    }
    // Ignore模式 追加100条
    generateData(100).writer.mode(SaveMode.Ignore).jdbc(table,driver,url,user,password)

    TraversableIO.read.jdbc[Data](table,driver,url,user,password).length == 100
  }

  //断言 Overwrite 数据100条
  assertTrue {
    generateData(100).writer.mode(SaveMode.Overwrite).jdbc(table,driver,url,user,password)
    TraversableIO.read.jdbc[Data](table,driver,url,user,password).length == 100
  }

  println("SUCCESS")
}
