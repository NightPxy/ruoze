package night.homework.lesson02_20180722_scalawc

import night.homework.common.io.SaveMode.SaveMode
import night.homework.common.io.{SaveMode, TraversableIO}
import night.homework.lesson02_20180721_scalaio.Data
import night.homework.lesson02_20180721_scalaio.ScalaIOTest.{driver, password, table, textOutputPath, url, user}

import scala.collection.mutable
import scala.io.Source
import scala.reflect.ClassTag

/**
  * Created by Administrator on 2018/7/24.
  */
object ScalaWc extends App {

  var toBuffer2: mutable.Buffer[String] = Source.fromFile("d:///log.txt").getLines().toBuffer

  val  a=toBuffer2
  /**
    * 作业: 读取文本中的数据 单词计数后将结果写入 MySQL
    */
  val textIntputPath = "D:\\data\\TestData\\data.txt";

  implicit def traversableSaveToFile[T: ClassTag](value: Traversable[T]) = TraversableIO(value);

  case class Wc(word: String, count: Int)

  //输入 一百万条
  val inputDataSet = TraversableIO.read.text[Data](textIntputPath)

  //计算
  val wcDataSet = inputDataSet
    .map(_.toString).flatMap(x => x.split("\t")) // Class转String 再将String压扁分割 => 源数据切割成独立单词
    .map(x => (x, 1)) // 独立单词 转 Tuple[Key,Value]
    .groupBy(x => x._1)  //按Key分组 => 相同单词压到一起
    .mapValues(x => x.map(y => y._2).sum)  // 对同单词下的记录计数 => 单数数量
    .map(x => Wc(x._1, x._2)) // 统计结果转换为 Wc(word: String, count: Int)

  //输出
  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false"
  val user = "root"
  val password = "12abAB"
  val table = "word_count"
  wcDataSet.writer.mode(SaveMode.Overwrite).jdbc(table,driver,url,user,password)

  println("统计完毕");
}
