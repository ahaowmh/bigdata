package value

import org.apache.spark.{SparkConf, SparkContext}

object FlatMap {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    //若使用yarn-client模式 master可以填local[*]
    //若使用yarn-cluster模式 master位置留空
    val conf = new SparkConf().setAppName("SparkCore").setMaster("local[*]")

    //2.创建SparkContext
    val sc = new SparkContext(conf)

    //3.编写任务代码
    val listRDD = sc.makeRDD(List(List(1, 2, 3), List(4, 5, 6)))

    //将匿名函数的返回值打散为单个元素

    //4.关闭SparkContext
    sc.stop()
  }
}
