package night.homework.common.scalikejdbc

import scalikejdbc.DB.using
import scalikejdbc.{ConnectionPool, DB, DBSession, GlobalSettings, LoggingSQLAndTimeSettings}

import scala.collection.mutable
import scala.concurrent.Future

/**
  * Scalikejdbc使用封装
  * 定位: 适合函数式编程的JDBC快速使用API
  *       非ORM向(没有以实体为中心的增删改查,没有实体关系或DSL方面内容,没有反向生成内容等)
  *
  * 执行模式:
  *   同步执行JDBC操作(execute)
  *       可以选择(execute.mod)以 无事务自动提交(auto) 事务提交异常回滚(tran) 只读模式(readOnly) 执行同步操作
  *   异步执行JDBC操作(executeAsync)
  *       在这种情况下,应该以 Future 方式传入操作,并且异步模式将默认使用事务提交并在之后的操作异常后回滚之前的操作
  *
  * 数据库连接:
  *   数据库连接将始终使用连接池获取.并且默认在放回连接池时自动关闭连接
  *   可以通过 con.autoClose=false 来关闭自动连接关闭功能
  *
  */
class ScalikejdbcContext(settings:ScalikejdbcConf) {

  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = settings.logEnabled,
    singleLineMode = false,
    printUnprocessedStackTrace = true,
    stackTraceDepth = 0,
    logLevel = settings.logLevel,
    warningEnabled = false,
    warningThresholdMillis = 3000L,
    warningLogLevel = 'warn)

  /**
    * 同步执行
    *
    * 示例:
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
            .bind(testcase.id, testcase.name, testcase.age)
            .update.apply

          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .list().apply()

          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .single().apply()
      }
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

  import scala.concurrent.ExecutionContext.Implicits.global
  /**
    * 异步执行
    *   1.执行必须以 future 块
    *   2.自带事务模式. 如果后执行异常将自动回滚前执行
    *
    * 例:
       ScalikejdbcContext(conf).executeAsync {
          implicit session =>
            Future {
              SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
                .bind(testcase.id, testcase.name, testcase.age)
                .update.apply
            }
            Future {
              SQL("select * from users where id = ?")
                .bind(testcase.id)
                .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
                .single().apply()
            }
            Future {
              throw new Exception(); //异常将会回滚前面提交
            }
        }
    */
  def executeAsync[T](execution: (DBSession) ⇒ Future[T]) = {
    Class.forName(settings.driver)
    ConnectionPool.singleton(settings.url, settings.user, settings.password)
    DB futureLocalTx execution
  }
}

object ScalikejdbcContext {
  def apply(conf:ScalikejdbcConf): ScalikejdbcContext = new ScalikejdbcContext(conf)
  def apply(opts: mutable.HashMap[String, String]): ScalikejdbcContext = new ScalikejdbcContext(ScalikejdbcConf(opts))
}
