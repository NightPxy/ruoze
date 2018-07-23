package night.homework.common.utils.impros

/**
  * Created by Administrator on 2018/7/23.
  */
case class HashMapImpr[TKey, TValue](hashMap: scala.collection.mutable.HashMap[TKey, TValue]) {

  def assertGet(key: TKey, msg: String = null) = hashMap.get(key) match {
    case Some(value) => value
    case None => if (msg == null) {
      throw new Throwable(s" [${key}]不能为空 ")
    } else {
      throw new Throwable(s" ${msg} 配置[${key}]不能为空 ")
    }
  }

  def set(key: TKey, value: TValue) = hashMap += key -> value
}
