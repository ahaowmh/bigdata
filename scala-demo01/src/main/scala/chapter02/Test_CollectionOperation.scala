package chapter02

object Test_CollectionOperation {
  def main(args: Array[String]): Unit = {
    val arr: Array[Int] = Array(12, 33, 45, 87)

    //把操作（函数）当做参数，对集合进行处理，返回一个新的集合
    def arrayOperation(array: Array[Int], op: Int => Int): Array[Int] = {
      for (elem <- array) yield op(elem)
    }

    //定义加操作
    def addOne(elem: Int): Int = {
      elem + 1
    }

    //调用集合和操作函数
    //第二个参数是传入函数本身，addOne _或者是addOne (上面定义此函数时，2参数已经明确定义为一个函数）
    val newArray = arrayOperation(arr, addOne _)
    //直接打印，是一个数组地址（引用类型）。打印数组，调用mkString方法
    //println(newArray)
    println(newArray.mkString(",")) //13,34,46,88

    //将匿名函数作为参数传入
    //val newArray2 = arrayOperation(arr, elem => elem * 2)
    val newArray2 = arrayOperation(arr, _ * 2)

    println(newArray2.mkString(","))//24,66,90,174
  }
}
