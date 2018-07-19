package night.homework.lesson01_20180717_scalikejdbc

import java.util.concurrent.ConcurrentHashMap

/**
  * Scalikejdbc 配置项
  *   "db.driver" -> db.JDBC驱动,必填
  *   "db.url" -> db.url,必填
  *   "db.user" -> db.用户名 必填;
  *   "db.password" -> db.密码 必填;
  *
  *   "execute.mod" -> 执行模式: auto:无事务自动提交  tran:事务提交异常回滚  readOnly:只读模式
  *
  *   "con.autoClose" -> 连接自动关闭(默认true,表示连接回到连接池前将自动关闭)
  *
  *   "logEnabled" -> 是否输出 Scalikejdbc 执行日志.选填,默认为 true
  *   "logLevel" -> 输出日志级别.选填 默认为 debug 级别
  */
case class ScalikejdbcConf() {

  private val options = new scala.collection.mutable.HashMap[String, String]()

  private case class optionHashMapImprovements(hashMap: scala.collection.mutable.HashMap[String, String]) {
    def assertGet(key: String) = hashMap.get(key) match {
      case Some(value) => value
      case None => throw new ScalikejdbcExecuteException(s" Scalikejdbc 配置[${key}]不能为空 ")
    }
  }

  private implicit def hashMapImprovements(hashMap: scala.collection.mutable.HashMap[String, String]) =
    optionHashMapImprovements(hashMap);

  val dbDriverKey = "db.driver"
  def driver() = options.assertGet(dbDriverKey)
  def driver(value: String) = {
    options += dbDriverKey -> value
    this
  }

  val dbUrlKey = "db.url"
  def url() = options.assertGet(dbUrlKey)
  def url(value: String) = {
    options += dbUrlKey -> value;
    this;
  }

  val dbUserKey = "db.user"
  def user() = options.assertGet(dbUserKey)
  def user(value: String) = {
    options += dbUserKey -> value;
    this;
  }

  val dbPasswordKey = "db.password"
  def password() = options.assertGet(dbPasswordKey)
  def password(value: String) = {
    options += dbPasswordKey -> value;
    this;
  }

  val executeModKey = "execute.mod"
  def executeMod() = options.getOrElse(executeModKey, "auto")
  /**
    * auto:无事务自动提交  tran:事务提交异常回滚  readOnly:只读模式
    */
  def executeMod(value: String) = {
    options += executeModKey -> value;
    this;
  }

  val conAutoCloseKey = "con.autoClose"
  def conAutoClose() = options.getOrElse(conAutoCloseKey, "true").toBoolean
  def conAutoClose(value: Boolean) = {
    options += conAutoCloseKey -> value.toString;
    this;
  }

  val logEnabledKey = "log.enabled"
  def logEnabled() = options.getOrElse(logEnabledKey, "true").toBoolean
  def logEnabled(value: Boolean) = {
    options += logEnabledKey -> value.toString;
    this;
  }

  val logLevelKey = "log.level"
  def logLevel() = Symbol(options.getOrElse(logLevelKey, "debug"))
  def logLevel(value: String) = {
    options += logLevelKey -> value.toString;
    this;
  }


  def conf(opts: Map[String, String]) = {
    opts.map(item=>this.options += item._1 -> item._2)
    this;
  }

}

object ScalikejdbcConf {
  def apply(conf: Map[String, String]): ScalikejdbcConf = new ScalikejdbcConf().conf(conf)
}
