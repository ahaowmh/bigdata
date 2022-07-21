package chapter04

import scala.::

object Test_List1 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)
    list.foreach(println)
    //添加元素
    val list1 = list :+ 5
    list1.foreach(println)
    //拼接list
    val list3 = 8 :: list1
    println(list3)//List(8, 1, 2, 3, 4, 5) 头插法

    //合并俩个list
    val list2 = list ::: list1
    println(list2) //List(1, 2, 3, 4, 1, 2, 3, 4, 5)
    //Nil空集合
    val list4 = 1 :: 2 :: 3 :: 4 ::Nil
    println(list4)//List(1, 2, 3, 4)

  }

}
