package chapter01

object Test_Variable {
  def main(args: Array[String]): Unit = {
    //1.声明变量时，类型可以省略，编译器会自动类型推导
    //2.声明变量时，必须给初始值
    val a : Int = 10
    var b  = 20

    //3.类型确定后，都不可以修改（强类型语言）
    //b = "aaasa" // 编译报错
    //4.声明/定义变量时，可以使用val或者var修饰。
    //var修饰的变量可以变，val不行。
    b = 40  //fine
    //a = 30 编译报错


    //当val修饰对象时，对象不可变，但对象的属性值可以变


  }
}
