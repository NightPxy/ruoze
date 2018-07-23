package night.homework.lesson01_20180717_scalikejdbc

import night.homework.common.scalikejdbc.{ScalikejdbcConf, ScalikejdbcContext}
import night.homework.common.test.TestUtils
import scalikejdbc.SQL

import scala.collection.mutable
import scala.concurrent.Future

/**
  * Created by Night on 2018/7/21.
  */
case class User(id: String, name: String, age: Int)

class UserTest(testcase: User) extends TestUtils {

  import scala.concurrent.ExecutionContext.Implicits.global


  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://127.0.0.1:3306/homework?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false"
  val user = "root"
  val password = "12abAB"

  def init() = {
    val conf = ScalikejdbcConf(mutable.HashMap(
      "db.driver" -> driver,
      "db.url" -> url,
      "db.user" -> user,
      "db.password" -> password
    ));

    ScalikejdbcContext(conf).execute {
      implicit session =>
        SQL("""CREATE TABLE IF NOT EXISTS `users` (
      `id` varchar(45) NOT NULL,
      `name` varchar(45)  NOT NULL,
      `age` int(11)  NOT NULL,
      PRIMARY KEY (`id`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8
      """).update.apply
    }

    println("****************************************************************")
    println("**************  Initialize UserTable Success *******************")
    println("****************************************************************")

    this
  }

  def crud() = {
    val conf = ScalikejdbcConf(mutable.HashMap(
      "db.driver" -> driver,
      "db.url" -> url,
      "db.user" -> user,
      "db.password" -> password
    ));

    //断言写入成功
    assertOptionNotNone {
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
    }

    //断言删除成功
    assertOptionIsNone {
      ScalikejdbcContext(conf).execute {
        implicit session =>

          SQL("delete from users where name = ?")
            .bind(testcase.name)
            .update.apply

          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .single().apply()
      }
    }

    println("****************************************************************")
    println("***********************  CRUD Success **************************")
    println("****************************************************************")

    this
  }

  def rollback() = {
    val conf = ScalikejdbcConf(mutable.HashMap(
      "db.driver" -> driver,
      "db.url" -> url,
      "db.user" -> user,
      "db.password" -> password,
      "execute.mod" -> "tran"
    ));

    //异常回滚写入
    assertException {
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
            .bind(testcase.id, testcase.name, testcase.age)
            .update.apply

          throw new Exception("测试异常回滚");
      }
    }
    //断言写入失败
    assertOptionIsNone {
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .single().apply()
      }
    }

    println("****************************************************************")
    println("*********************  rollback Success ************************")
    println("****************************************************************")

    this
  }

  def readOnly() = {
    val conf = ScalikejdbcConf(mutable.HashMap(
      "db.driver" -> driver,
      "db.url" -> url,
      "db.user" -> user,
      "db.password" -> password,
      "execute.mod" -> "readOnly"
    ));

    //断言查询成功
    assertOptionIsNone {
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .list().apply()

          SQL("select * from users where id = ?")
            .bind(testcase.id)
            .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
            .single().apply()
      }
    }

    //断言写入失败
    assertException {
      ScalikejdbcContext(conf).execute {
        implicit session =>
          SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
            .bind(testcase.id, testcase.name, testcase.age)
            .update.apply
      }
    }

    println("****************************************************************")
    println("*********************  readOnly Success ************************")
    println("****************************************************************")

    this
  }

  def async() = {
    val conf = ScalikejdbcConf(mutable.HashMap(
      "db.driver" -> driver,
      "db.url" -> url,
      "db.user" -> user,
      "db.password" -> password
    ));

    //断言写入异步成功且查询成功
    assertOptionNotNone {
      await {
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
        }
      }
    }
    //断言删除异步成功且查询失败
    assertOptionIsNone {
      await {
        ScalikejdbcContext(conf).executeAsync {
          implicit session =>
            Future {
              SQL("delete from users where name = ?")
                .bind(testcase.name)
                .update.apply
            }
            Future {
              SQL("select * from users where id = ?")
                .bind(testcase.id)
                .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
                .single().apply()
            }
        }
      }
    }
    //断言写入异步失败回滚
    assertException {
      await {
        ScalikejdbcContext(conf).executeAsync {
          implicit session =>
            Future {
              SQL("replace into `users` (`id`,`name`,`age`) VALUES (?,?,?);")
                .bind(testcase.id, testcase.name, testcase.age)
                .update.apply
            }
            Future {
              throw new Exception("测试异常回滚");
            }
        }
      }
    }
    //断言异步成功且查询失败
    assertOptionIsNone {
      await {
        ScalikejdbcContext(conf).executeAsync {
          implicit session =>
            Future {
              SQL("select * from users where id = ?")
                .bind(testcase.id)
                .map(rs => User(rs.string("id"), rs.string("name"), rs.int("age")))
                .single().apply()
            }
        }
      }
    }

    println("****************************************************************")
    println("***********************  async Success *************************")
    println("****************************************************************")

    this
  }
}


