package chapter04

import scala.collection.immutable.HashMap

object Test_Map {
  def main(args: Array[String]): Unit = {
    val hashMap = new HashMap[String, Int]()
    val map = Map("张三" -> 10, "李四" -> 20)

    val maybeInt = map.get("张三")
    println(maybeInt)//Some(10)
    val maybeInt1 = map.get("张三a")
    println(maybeInt1)//None

    val i = maybeInt1.getOrElse(3)
    println(i)//3

    //合并get和getOrElse
    val i1 = map.getOrElse("张三", 8)
    println(i1)//10

  }

}
