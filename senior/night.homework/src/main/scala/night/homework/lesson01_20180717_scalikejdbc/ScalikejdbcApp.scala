package night.homework.lesson01_20180717_scalikejdbc

import night.homework.common.scalikejdbc.{ScalikejdbcConf, ScalikejdbcContext}
import scalikejdbc.SQL

import scala.collection.mutable
import scala.collection.mutable._


/**
  * Created by Night on 2018/7/18.
  */
object ScalikejdbcApp extends App {

  /**
    * 作业: Scalikejdbc => User(id: String, name: String, age: Int)  封装增删改查
    */
  val user = User("user_id", "user_name", 1);

  import scala.concurrent.ExecutionContext.Implicits.global

  val conf = ScalikejdbcConf(mutable.HashMap(
    "db.driver" -> "com.mysql.jdbc.Driver",
    "db.url" -> "jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false",
    "db.user" -> "root",
    "db.password" -> "12abAB",
    "log.enabled" -> "false"
  ));

  ScalikejdbcContext(conf).execute {
    implicit session =>
      SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
        .bind(user.id, user.name, user.age)
        .update.apply
  }

  println("*******************  Single Query ******************** ")
  println{
    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL("select * from users where id = ?")
          .bind(user.id)
          .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
          .single().apply()
    }
  }
  println("*******************  List Query ******************** ")
  println{
    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL("select * from users where id = ?")
          .bind(user.id)
          .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
          .list().apply()
    }
  }

}





