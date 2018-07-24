package night.homework.common.io.readwrite.jdbc

import java.util.Date

import night.homework.common.io.{ReadWriteConf, SaveMode, TraversableReadAble, TraversableWriteAble}
import night.homework.common.scalikejdbc.{ScalikejdbcConf, ScalikejdbcContext}
import night.homework.common.utils.patterns.UsingPattern
import scalikejdbc.SQL

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.reflect.{ClassTag, classTag}

/**
  * Created by Night on 2018/7/23.
  */
private[io] class JdbcTraversableReadWriter
  extends TraversableWriteAble with TraversableReadAble with UsingPattern {
  implicit def readWriteConf(conf: mutable.HashMap[String, String]) = ReadWriteConf(conf)

  implicit def jdbcConf(conf: mutable.HashMap[String, String]) = ScalikejdbcConf(conf)

  private def isExistsOutputTable(table: String, conf: mutable.HashMap[String, String]): Boolean = {
    try {
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL(s" select 1 from $table where 1=2").map(rs => rs.int("a")).single().apply()
      } match {
        case Some(_) => true
        case None => true
      }
    } catch {
      case _:Throwable => false
    }
  }

  private def createTable[T: ClassTag](conf: mutable.HashMap[String, String]) = {
    val table = conf.outputJdbcTable;
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    val sql = new StringBuilder().append(s"create table if not exists `${table}` ( ")
    runtimeClassFields.map(field => {
      sql.append(s" `${field.getName}` ")
      if (field.getGenericType.isInstanceOf[Int] == classOf[Int]) {
        sql.append(s" int(11) NULL")
      }
      else {
        sql.append(s" varchar(255) NULL")
      }
      sql.append(s",")
    })
    sql.deleteCharAt(sql.length - 1)
    sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8")

    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL(sql.toString).update().apply()
    }
  }

  private def dropTable(conf: mutable.HashMap[String, String]) = {
    val table = conf.outputJdbcTable;
    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL(s" drop table `$table` ").update().apply()
    }
  }
  private def write[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]) = {
    createTable(conf)
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    runtimeClassFields.map(field => field.setAccessible(true))
    val table = conf.outputJdbcTable;

    ScalikejdbcContext(conf).execute {
      implicit session =>
        traversableData.map(element => {
          val sql = new StringBuilder()
          sql.append(s"insert into `${table}` ( ")
          val listBuffer = ListBuffer[Any]();
          runtimeClassFields.map(field => {
            sql.append(s" `${field.getName}` ,")
          })
          sql.deleteCharAt(sql.length - 1)
          sql.append(s" ) values ( ")
          runtimeClassFields.map(field => {
            sql.append(s" ?,")
            listBuffer += field.get(element)
          })
          sql.deleteCharAt(sql.length - 1)
          sql.append(")")
          SQL(sql.toString).bind(listBuffer: _*).update().apply()
        })
    }
  }

  override def save[T: ClassTag](traversableData: Traversable[T], conf: mutable.HashMap[String, String]): Unit = {

    val table = conf.outputJdbcTable;
    conf.outputMode match {
      case SaveMode.ErrorIfExists => if (isExistsOutputTable(table, conf)) throw new Exception(s"jdbc写入失败 ${table} 已经存在")
      case SaveMode.Ignore => if (!isExistsOutputTable(table, conf)) write(traversableData, conf)
      case SaveMode.Append => write(traversableData, conf)
      case SaveMode.Overwrite => if (isExistsOutputTable(table, conf)) dropTable(conf); write(traversableData, conf)
    }
  }

  override def read[T: ClassTag](conf: mutable.HashMap[String, String]): List[T] = {
    val table = conf.inputJdbcTable;
    if (!isExistsOutputTable(table, conf)) {
      throw new Throwable(s"表 ${table} 不存在");
    }
    val runtimeClass = classTag[T].runtimeClass
    val runtimeClassFields = runtimeClass.getDeclaredFields
    runtimeClassFields.map(field => field.setAccessible(true))


    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL(s"select * from ${table}").map(rs => {
          val element = runtimeClass.newInstance().asInstanceOf[T]
          runtimeClassFields.map(field => {
            if (field.getGenericType == classOf[Date]) {
              field.set(element, rs.date(field.getName))
            }
            else if (field.getGenericType == classOf[Int]) {
              field.set(element, rs.int(field.getName))
            }
            else {
              field.set(element, rs.string(field.getName))
            }
          })
          element
        }).list().apply()
    }
  }
}

object JdbcTraversableReadWriter {
  def apply(): JdbcTraversableReadWriter = new JdbcTraversableReadWriter()
}
