package night.homework.lesson02_20180721_scala2file

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import night.homework.common.io.TraversableWriteAble
import night.homework.common.utils.RandomUtils

/**
  * Created by Night on 2018/7/22.
  */

case class TestData(domain: String, traffic: String, time: Date) extends TraversableWriteAble {
  def writeLine(): String = s"${domain}\t${traffic}\t${TestData.dateFormat.format(time)}";
}
object TestData extends RandomUtils {
  private val dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")
  private val domains = Seq("www.ruozedata.com", "www.zhibo8.com", "www.dongqiudi.com")
  private def randomDate = {
    var cal = Calendar.getInstance()
    cal.setTime(new Date())
    cal.add(Calendar.DAY_OF_MONTH, -5 + random(3))
    cal.getTime()
  }
  def randomTestData = {
    if (random(10000) < 10) {
      //千分之一错误率
      TestData(
        "xxoo" + domains(random(3)),
        "abcd" + (1000 + random(10) * 100).toString,
        randomDate
      )
    }
    else {
      TestData(
        domains(random(3)),
        (1000 + random(10) * 100).toString,
        randomDate
      )
    }

  }
}
