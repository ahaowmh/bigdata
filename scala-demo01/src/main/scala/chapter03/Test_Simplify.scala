package chapter03

object Test_Simplify {
  def main(args: Array[String]): Unit = {
    def f0(name: String): String = {
      return name
    }

    println(f0("tom"))

    //默认函数体最后一行代码作为返回值。return可以省略
    def f1(name: String): String = {
      name
    }

    println(f1("jack"))

    //函数体只有一行代码，花括号可以省略
    def f2(name: String): String = name

    println(f2("alinda"))

    //当返回值类型可以推断出来，返回值类型可以省略
    //函数式只关心数据映射关系，f(x) = x 给定一个x,通过映射关系，一定能返回一个确定的值
    def f3(name: String) = name

    println(f3("alice"))

    //有return时，函数返回值类型不可以省略
    //若返回值类型声明为Unit,有return也不起作用  打印返回为（）

    //如果期望无返回值类型，= 也可以省略  --->过程
    def f6(name: String) {
      println(name)
    }

    println(f6("karolin")) //先打印karolin,再打印（）

    //函数无参，不声明参数列表，调用时也不可加（）
    //函数无参，声明了参数列表，调用时可加（）也可以不加

    //如果只关心逻辑处理，函数名称和def也可以省略 --->匿名函数，lambda表达式
    (num: Int) => num * 3

  }

}
