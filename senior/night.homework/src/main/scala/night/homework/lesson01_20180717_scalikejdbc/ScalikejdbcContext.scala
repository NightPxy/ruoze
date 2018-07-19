package night.homework.lesson01_20180717_scalikejdbc

import scalikejdbc.DB.{CPContext, NoCPContext, using}
import scalikejdbc.{ConnectionPool, DB, DBConnection, DBSession, GlobalSettings, LoggingSQLAndTimeSettings, SettingsProvider}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Administrator on 2018/7/19.
  * 定位:适合闭包的快速使用API
  * 所以非ORM向(没有以实体为中心的增删改查,没有实体关系或DSL方面内容,没有反向生成内容)
  * 连接池 只读 事务 异步  批量
  *
  * 连接模式:自动关闭(每次执行开启新的).
  * 执行模式:默认,只读,事务
  * 同步/异步
  */
class ScalikejdbcContext(settings:ScalikejdbcConf) {
  /** ********************************** PUBLIC ******************************************/

  def conf(opts: Map[String, String]) = {
    settings.conf(opts)

    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
      enabled = settings.logEnabled,
      singleLineMode = false,
      printUnprocessedStackTrace = true,
      stackTraceDepth = 0,
      logLevel = settings.logLevel,
      warningEnabled = false,
      warningThresholdMillis = 3000L,
      warningLogLevel = 'warn)

    this;
  }

  /**
    * 同步执行
    */
  def execute[T](execution: DBSession => T) = {

    Class.forName(settings.driver)
    ConnectionPool.singleton(settings.url, settings.user, settings.password)

    using(ConnectionPool.borrow()) { conn =>
      val dbConnection = DB(conn).autoClose(settings.conAutoClose)
      settings.executeMod match {
        case "auto" => dbConnection autoCommit execution
        case "tran" => dbConnection localTx execution
        case "readOnly" => dbConnection readOnly execution
        case _ => throw new ScalikejdbcExecuteException(s"未受支持的executeMod: ${settings.executeMod}")
      }
    }
  }

  /**
    * 异步执行
    *   1.执行必须以 future 块
    *   2.自带事务模式. 如果后执行异常将自动回滚前执行
    */
  def executeAsync[T](execution: (DBSession) ⇒ Future[T])(implicit ec: ExecutionContext) = {
    Class.forName(settings.driver)
    ConnectionPool.singleton(settings.url, settings.user, settings.password)
    using(ConnectionPool.borrow()) { conn =>
      val dbConnection = DB(conn).autoClose(settings.conAutoClose)
      dbConnection futureLocalTx execution
    }
  }
}

object ScalikejdbcContext {
  def apply(conf:ScalikejdbcConf): ScalikejdbcContext = new ScalikejdbcContext(conf)
  //def apply(opts: Map[String, String]): ScalikejdbcContext = new ScalikejdbcContext().conf(opts)
}
