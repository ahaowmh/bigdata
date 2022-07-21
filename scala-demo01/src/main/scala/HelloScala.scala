object HelloScala {
  def main(args: Array[String]): Unit = {
    println("hello world")

    //Unit 返回值为空，对应Java中的void
    def m1() = {
      println("m1被调用")
    }

    val a = m1()
    println(a)


    //Nothing 所有类型的子类型
    def m2(n: Int): Int = {  //如果n等于0，抛出异常，什么都不返回（Nothing)
      if (n == 0)
        throw new NullPointerException
      else
        return n
    }

    val i: Int = m2(3)
    println(i)

    val b = m2(0)
    println(b)
  }
}