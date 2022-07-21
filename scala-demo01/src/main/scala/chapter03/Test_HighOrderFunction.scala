package chapter03

object Test_HighOrderFunction {
  def main(args: Array[String]): Unit = {
    //将函数作为函数返回值返回
    def f1(): Int => Unit = {
      def f2(a: Int): Unit = {
        println(s"f2被调用$a")
      }
      //f2 _  //返回函数f2本身
      f2 //根据f1的返回值类型可以确实此处为函数，可以不用加 _
    }
    val f2 = f1()  //f1返回值为f2。所以下面打印f1()或者f2结果都一样，都是f2函数本身
    println(f2)//函数本身底层也是一个对象chapter03.Test_HighOrderFunction$$$Lambda$1/931919113@7cf10a6f
    println(f1())//chapter03.Test_HighOrderFunction$$$Lambda$1/931919113@7cf10a6f
    //想要打印调用f2以及f2函数的返回值
    println(f2(8))//f2被调用8  ()
    //也可一步到位打印调用f2以及f2函数的返回值
    println(f1()(6))//f2被调用6  ()
  }

}
