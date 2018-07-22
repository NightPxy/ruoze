package night.homework.lesson01_20180717_scalikejdbc

import scala.collection.mutable._


/**
  * Created by Night on 2018/7/18.
  */
object Startup extends App {

  val a = HashMap

  val user = User("userTest", "userTest", 1);
  val test = new UserTest(user).init()
  test
    .crud()
    .rollback()
    .readOnly()
    .async()

}



