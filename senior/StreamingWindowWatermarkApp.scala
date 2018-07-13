package night

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, TimeZone}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Spark Streaming 模拟结构化流中 基于事件时间的Window+WaterMark 完成WorkCount功能
  *   个人理解在生产中 基于事件时间处理和数据延时到达 是一种必然要求 在没有或无法启用结构化流的情况下,尝试以Spark Streaming 完成
  *
  * 核心计算过程:
  *   1.StreamingWindowWatermarkUtils.windowTime 计算出 依据输入数据的事件时间,应该落入的窗口范围
  *   2.按照(窗口范围+word)分组,统计本批次的基于窗口的WordCount计算结果
  *   3.将本批次的计算结果与CheckPoint中的历史结果合并,策略如下:
  *       对没有在本批次出现的其它历史记录
  *         判断其word窗口是否处于水印有效期中,如果是则保留,否则移除(水印过期)
  *       对本批次的结果
  *         如果当前结果已过期,历史记录已过期 则移除   <=(水印过期)
  *         如果当前结果已过期,历史记录未过期 则保留历史记录  <=(当前结果水印过期)
  *         如果当前结果未过期,历史记录已过期 则将当前结果覆盖历史记录   <=(历史记录水印过期)
  *         如果当前结果未过期,历史记录未过期 则合并(累加)当前结果和历史记录
  *
  */
object StreamingWindowWatermarkApp extends App {

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd#HH:mm:ss");
  dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))

  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  val spark = SparkSession
    .builder()
    .appName("streaming-window-watermark-app")
    .master("local[2]")
    .config("spark.shuffle.sort.bypassMergeThreshold", 2)
    .getOrCreate()

  import spark.implicits._;
  import StreamingWindowWatermarkUtils._;

  val ssc = new StreamingContext(spark.sparkContext, Seconds(5))
  //这里必须是HDFS,开发环境中先用本地文件系统替代下
  ssc.checkpoint("D:\\data\\StremingCheckpoint\\StreamingApp")

  val inputs = ssc.socketTextStream("127.0.0.1", 9999)


  //输入格式化
  case class InputRow(id: Long, word: String, time: Date)
  val inputRows = inputs
    .flatMap(_.split(" "))
    .map(_.split("\\|"))
    .map(rowSpt => try {
      InputRow(rowSpt(0).toLong, rowSpt(1), dateFormat.parse(rowSpt(2)))
    } catch {
      case ex:Throwable => InputRow(0,ex.getMessage , null)
    })


  //计算过程
  case class ComputeRow(word: String,winStartTime:Date,winEndTime: Date, count: Int)
  val windowDuration = 60 //窗口范围1分钟
  val waterMarkDuration = 60*3 //水印持续3分钟

  val noneResultMerge = (historyOpt: Option[ComputeRow])=>
    historyOpt match {
      case None => None
      case Some(history) => if (isWaterMarkExpired(history.winEndTime, waterMarkDuration)) {  None }
      else { Some(history) }
    }

  val existsResultMerge = (values: Seq[ComputeRow], historyOpt: Option[ComputeRow])=>{
    val value = values.headOption.getOrElse(null)
    val computeValues = values.filter(value=> !isWaterMarkExpired(value.winEndTime, waterMarkDuration)).map(value=>value.count)
    historyOpt match {
      case None =>
        if (computeValues.length == 0) {
          None
        }
        else {
          Some(ComputeRow(value.word, value.winStartTime, value.winEndTime, computeValues.sum))
        }
      case Some(history) =>
        if (isWaterMarkExpired(history.winEndTime, waterMarkDuration)) {
          Some(ComputeRow(value.word, value.winStartTime, value.winEndTime, computeValues.sum))
        }
        else {
          Some(ComputeRow(value.word, value.winStartTime, value.winEndTime, computeValues.sum + history.count))
        }
    }

  }

  val computeRows = inputRows
    .filter(row => row.id > 0 && !isWaterMarkExpired(row.time, waterMarkDuration))
    .map(row => {
      val winTime = windowTime(row.time, windowDuration)
      val windowKey = s"${row.word}|${winTime._1}|${winTime._2}"
      (windowKey, ComputeRow(row.word,winTime._1,winTime._2, 1))
    })
    //本批次计算WordCount
    .reduceByKey((v1, v2) => ComputeRow(v1.word,v1.winStartTime,v1.winEndTime, v1.count + v2.count))
    //本批次计算结果与历史记录合并
    .updateStateByKey((values: Seq[ComputeRow], stateOpt: Option[ComputeRow]) => {
      if(values.length == 0){
        noneResultMerge(stateOpt)
      }
      else {
        existsResultMerge(values,stateOpt)
      }
    })

  //结果输出
  computeRows.print();

  /**
    *  运行演示如下
      2018-07-13 23:55:35 输入数据如下:
        1|aaa|2018-07-13#23:53:31
        1|aaa|2018-07-13#23:53:21
        1|aaa|2018-07-13#23:53:11
        1|aaa|2018-07-13#23:53:01
        1|aaa|2018-07-13#23:55:01
        1|aaa|2018-07-13#23:55:01

      计算触发3秒,窗口范围1分钟,水印延时3分钟,计算结果如下:
      -------------------------------------------
      Time: 1531497335000 ms   2018-07-13 23:55:35 (23:53:31,23:55:01都参与计算,23:53:31视为到23:55:35才到达的延时数据)
      -------------------------------------------
      (aaa|Fri Jul 13 23:55:00 CST 2018|Fri Jul 13 23:56:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:55:00 CST 2018,Fri Jul 13 23:56:00 CST 2018,1))
      (aaa|Fri Jul 13 23:53:00 CST 2018|Fri Jul 13 23:54:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:53:00 CST 2018,Fri Jul 13 23:54:00 CST 2018,4))

      -------------------------------------------
      Time: 1531497415000 ms  2018-07-13 23:56:55
      -------------------------------------------
      (aaa|Fri Jul 13 23:55:00 CST 2018|Fri Jul 13 23:56:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:55:00 CST 2018,Fri Jul 13 23:56:00 CST 2018,2))
      (aaa|Fri Jul 13 23:53:00 CST 2018|Fri Jul 13 23:54:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:53:00 CST 2018,Fri Jul 13 23:54:00 CST 2018,4))

      -------------------------------------------
      Time: 1531497420000 ms  2018-07-13 23:57:00 (23:53:31 离开计算,因为水印只延时到23:54:00,再之前已视为无效数据)
      -------------------------------------------
      (aaa|Fri Jul 13 23:55:00 CST 2018|Fri Jul 13 23:56:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:55:00 CST 2018,Fri Jul 13 23:56:00 CST 2018,2))

      -------------------------------------------
      Time: 1531497535000 ms 2018-07-13 23:58:55
      -------------------------------------------
      (aaa|Fri Jul 13 23:55:00 CST 2018|Fri Jul 13 23:56:00 CST 2018,ComputeRow(aaa,Fri Jul 13 23:55:00 CST 2018,Fri Jul 13 23:56:00 CST 2018,2))

      -------------------------------------------
      Time: 1531497540000 ms 2018-07-13 23:59:00 (所有数据都已离开)
      -------------------------------------------
    */

  ssc.start()
  ssc.awaitTermination();
  spark.close();
}
object StreamingWindowWatermarkUtils {

  /**
    * 指定时间是否水印过期
    * @return true:已过期  false:未过期
    */
  def isWaterMarkExpired(targetTime: Date, waterMarkDuration: Int): Boolean = {
    return targetTime.getTime < waterMarkExpired(waterMarkDuration).getTime
  }

  /**
    * 获取当前的水印过期时间点
    */
  def waterMarkExpired(waterMarkDuration: Int): Date = {
    val waterMarkExpired: Calendar = Calendar.getInstance()
    waterMarkExpired.add(Calendar.SECOND, -waterMarkDuration)
    waterMarkExpired.getTime
  }

  /**
    * 计算指定时间的窗口范围
    * 一个窗口长度为2秒的连续10秒窗口范围计算如下:
      计算时间:2018-07-13#23:00:01 窗口范围:2018-07-13#23:00:00 -> 2018-07-13#23:00:02
      计算时间:2018-07-13#23:00:02 窗口范围:2018-07-13#23:00:02 -> 2018-07-13#23:00:04
      计算时间:2018-07-13#23:00:03 窗口范围:2018-07-13#23:00:02 -> 2018-07-13#23:00:04
      计算时间:2018-07-13#23:00:04 窗口范围:2018-07-13#23:00:04 -> 2018-07-13#23:00:06
      计算时间:2018-07-13#23:00:05 窗口范围:2018-07-13#23:00:04 -> 2018-07-13#23:00:06
      计算时间:2018-07-13#23:00:06 窗口范围:2018-07-13#23:00:06 -> 2018-07-13#23:00:08
      计算时间:2018-07-13#23:00:07 窗口范围:2018-07-13#23:00:06 -> 2018-07-13#23:00:08
      计算时间:2018-07-13#23:00:08 窗口范围:2018-07-13#23:00:08 -> 2018-07-13#23:00:10
      计算时间:2018-07-13#23:00:09 窗口范围:2018-07-13#23:00:08 -> 2018-07-13#23:00:10
      计算时间:2018-07-13#23:00:10 窗口范围:2018-07-13#23:00:10 -> 2018-07-13#23:00:12
    * @return (windowStart,windowEnd)
    */
  def windowTime(targetTime: Date, windowDuration: Long): (Date, Date) = {
    val start = new Date(targetTime.getTime - (targetTime.getTime % (windowDuration * 1000)))
    val end = new Date(start.getTime + windowDuration * 1000)
    return (start, end)
  }
}