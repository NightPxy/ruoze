package night.homework.lesson01_20180717

import scalikejdbc.{ConnectionPool, DB}
import scalikejdbc._

/**
  * Created by Night on 2018/7/18.
  */
object Startup extends App {





  DbContext().unitOfWork {
    sql"""CREATE TABLE IF NOT EXISTS `users` (
      `id` varchar(45) NOT NULL,
      `name` varchar(45)  NOT NULL,
      `age` int(11)  NOT NULL,
      PRIMARY KEY (`id`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8
      """.update.apply
  }

//

   // implicit session =>

}

class DbContext() {
  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = false,
    printUnprocessedStackTrace = true,
    stackTraceDepth= 0,
    logLevel = 'debug,
    warningEnabled = false,
    warningThresholdMillis = 3000L,
    warningLogLevel = 'warn
  )

  def unitOfWork[T](execution: ()=> T)(implicit session: DBSession = AutoSession) = {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false", "root", "12abAB")
    DB localTx {
      implicit session =>
         execution
    }
  }
}

object DbContext {
  def apply(): DbContext = new DbContext()
}
