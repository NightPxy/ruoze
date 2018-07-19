package night.homework.lesson01_20180717_scalikejdbc

import scalikejdbc.{ConnectionPool, DB}
import scalikejdbc._

import scala.concurrent.Future

/**
  * Created by Night on 2018/7/18.
  */
object Startup extends App {

  val conf = ScalikejdbcConf(Map(
    "db.driver" -> "com.mysql.jdbc.Driver",
    "db.url" -> "jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false",
    "db.user" -> "root",
    "db.password" -> "12abAB"
  ));

  println("***********初始化数据库测试表**************")
  ScalikejdbcContext(conf).execute {
    implicit session =>
      sql"""CREATE TABLE IF NOT EXISTS `users` (
      `id` varchar(45) NOT NULL,
      `name` varchar(45)  NOT NULL,
      `age` int(11)  NOT NULL,
      PRIMARY KEY (`id`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8
      """.update.apply
  }

  case class User(id: String, name: String, age: Int);
  val userCRUD = User("userCRUD", "userCRUD", 1);

  println("***********事务下的增删改查批量**************")
  ScalikejdbcContext {
    conf.executeMod("readOnly")
  }.execute {
    implicit session =>

      //增改
      SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
        .bind(userCRUD.id, userCRUD.name, userCRUD.age)
        .update.apply

      //查询 单一实体
      SQL("select * from users where id = ?")
        .bind(userCRUD.id)
        .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
        .single().apply()
      //查询集合
      SQL("select * from users where id = ?")
        .bind(userCRUD.id)
        .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
        .list().apply()

  }

  println("***********事务下的异常回滚**************")
  ScalikejdbcContext {
    conf.executeMod("tran")
  }.execute {
    implicit session =>
      //删除
      SQL("delete from users where name = ?")
        .bind(userCRUD.name)
        .update.apply

      SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
        .bind(userCRUD.id, userCRUD.name, userCRUD.age)
        .update.apply
      throw new Exception("测试异常回滚");
  }

  println("***********异步事务下的增删改查和事务回滚**************")
  //  scalikejdbcContext.async{
  //    implicit session => {
  //      Future {
  //        SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
  //          .bind(userCRUD.id,userCRUD.name,userCRUD.age)
  //          .update.apply
  //      }
  //      Future {
  //        SQL("delete from users where name = ?")
  //          .bind(userCRUD.name)
  //          .update.apply
  //      }
  //      Future {
  //        throw new Exception("测试异常回滚");
  //      }
  //    }
  //  }
}

