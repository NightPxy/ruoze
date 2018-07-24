package night.homework.lesson01_20180717_scalikejdbc

import scala.collection.mutable._


/**
  * Created by Night on 2018/7/18.
  */
object ScalikejdbcApp extends App {

  /**
    * 作业: Scalikejdbc => User(id: String, name: String, age: Int)  封装增删改查
    */
  val user = User("userTest", "userTest", 1);
  val test = new UserTest(user).init()
  test
    .crud()
    .rollback()
    .readOnly()
    .async()

}





