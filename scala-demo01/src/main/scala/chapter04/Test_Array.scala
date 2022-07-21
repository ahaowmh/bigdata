package chapter04

object Test_Array {
  def main(args: Array[String]): Unit = {
    //不可变数组
    //创建方式一   元素类型和长度不可变
    val array = new Array[Int](8)
    //创建方式二   使用伴生对象的apply方法(底层也是逐个new）
    val array1 = Array(1, 2, 3, 4, 5)

    //遍历读取
    for (elem <- array1) {
      println(elem)
    }

    //使用迭代器遍历
    val iterator = array1.iterator

    while (iterator.hasNext) {
      println(iterator.next())
    }

    //修改数组元素的值
    println(array1(0)) // 1
    array1(0) = 10
    println(array1(0)) // 10
    //println(array1(0) = 9) // ()

    println("=========")

    //添加元素
    //array1不变，修改后的返回新数组
    val array2 = array1 :+ 7

    for (elem <- array2) {
      println(elem)
    }

  }
}
