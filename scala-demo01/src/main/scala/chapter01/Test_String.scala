package chapter01

object Test_String {
  def main(args: Array[String]): Unit = {
    var name = "ahao"
    var age = 18

    //字符串拼接
    println(age + "岁的" + name + "在快乐编程")

    //printf(),字符串，通过%传值
    printf("%d岁的%s热爱学习",age ,name)
    println()

    //模板字符串
    println(s"${age}岁的${name}在上海过得很好")

    val num  =  2.3456//默认为double
    //格式化模板字符串
    println(f"this num is ${num}%1.2f")

    //原始字符串模板
    println(raw"this num is ${num}%1.2f")

    //三个双引号，保持原始格式
    println(
      s"""
        |select *
        |from
        | student
        |where
        | name = ${name}
        |and
        | age > ${age}
        |""".stripMargin
    )
  }

}
