package night.homework.lesson02_20180721_scalaio

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import night.homework.common.utils.RandomUtils

/**
  * Created by Night on 2018/7/22.
  */

class Data()
{
  var domain: String = _
  var traffic: String = _
  var time: Date = _

  override def toString() = s"domain:$domain\ttraffic:$traffic\ttime:$time"
}

object Data extends RandomUtils {
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
      new Data() {
        domain = "xxoo" + domains(random(3))
        traffic = "abcd" + (1000 + random(10) * 100).toString
        time = randomDate
      }
    }
    else {
       new Data() {
        domain = domains(random(3))
        traffic = (1000 + random(10) * 100).toString
        time = randomDate
      }
    }
  }
}
