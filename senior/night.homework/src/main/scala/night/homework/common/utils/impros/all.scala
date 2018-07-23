package night.homework.common.utils.impros

/**
  * Created by Administrator on 2018/7/23.
  */
object all {
  implicit def hashMapImprovements[TKey,TValue](hashMap: scala.collection.mutable.HashMap[TKey, TValue]) =  HashMapImpr(hashMap);
}
