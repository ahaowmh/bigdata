package chapter04

import scala.collection.mutable.ArrayBuffer

object Test_ArrayBuffer {
  def main(args: Array[String]): Unit = {
    //可变数组
    //使用可变数组需要提前导包
    val arrayBuffer = new ArrayBuffer[Int]()
    val arrayBuffer1: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4, 5)

    //向数组中添加元素 append
    arrayBuffer.append(10,30)
    arrayBuffer1.appendAll(Array(6,6,6))

    //遍历打印
    arrayBuffer.foreach(println)
    arrayBuffer1.foreach(println)

    println(arrayBuffer1) //ArrayBuffer(1, 2, 3, 4, 5, 6, 6, 6) 底层调用了toString方法

    //修改元素 update
    arrayBuffer1.update(0,900)
    arrayBuffer1(1) = 222
    println(arrayBuffer1) //ArrayBuffer(900, 222, 3, 4, 5, 6, 6, 6)

    //查看元素
    println(arrayBuffer1(0))

    //删除元素 remove
    arrayBuffer1.remove(0,2)
    println(arrayBuffer1) //ArrayBuffer(3, 4, 5, 6, 6, 6)

    val arrays2: Array[Array[Int]] = Array.ofDim[Int](3, 4)
    for (array <- arrays2) {
      for (elem <- array) {
        print(elem + "\t")
      }
      println()
    }
    val array = Array.ofDim[String](3, 4, 5)

  }
}
