package chapter03

/*
 可变参数
*/
object Test_FunctionParameter {
  def main(args: Array[String]): Unit = {
    //此时的String*已经是一个集合
    def f1(str: String*): Unit = {
      println(str)
    }

    //WrappedArray包装数组。
    f1("alice") //WrappedArray(alice)
    f1("aaa", "bbb", "ccc") //WrappedArray(aaa, bbb, ccc)

    //如果参数列表中有多个不同类型的参数，将可变参数放在最后
    def f2(str1: String, str2: String*): Unit = {
      println("str1: " + str1 + "\n" + "str2: " + str2)
    }

    f2("alinda")
    f2("aaa","bbb","ccc")

    //给参数默认值
    def f3(kind: String = "good") = {
      println(s"i am a $kind man")
    }

    f3() //i am a good man
    f3("bad") //i am a bad man

  }

}
