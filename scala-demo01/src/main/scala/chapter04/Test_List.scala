package chapter04

object Test_List {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, 3, 4, 5, 6)
    val list2 = List(4, 5, 6, 7, 8,9)

    //获取集合的头  尾（除去头）
    println(list1.head)
    println(list1.tail)//List(2, 3, 4, 5, 6)
    println(list1.tails.next())//<iterator>/List(1, 2, 3, 4, 5, 6)

    //获取集合的最后一个数据  初始数据（不含最后一个）
    println(list1.last)
    println(list1.init)

    //反转
    println(list1.reverse)

    //取前（后）n个元素
    println(list1.take(2))
    println(list1.takeRight(2))

    //去除前（后）n 个元素
    println(list1.drop(3))
    println(list1.dropRight(3))

    //并集
    println(list1.union(list2))

    //交集
    println(list1.intersect(list2))

    //拉链  元素不相等时，去除
    println(list1.zip(list2))

    //滑窗
    list1.sliding(3,2).foreach(println)
  }


}
