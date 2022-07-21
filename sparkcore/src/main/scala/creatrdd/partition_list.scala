package creatrdd

import org.apache.spark.{SparkConf, SparkContext}

object partition_list {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    //若使用yarn-client模式 master可以填local[*]
    //若使用yarn-cluster模式 master位置留空
    val conf = new SparkConf().setAppName("SparkCore").setMaster("local[*]")


    //2.创建SparkContext
    val sc = new SparkContext(conf)

    //3.编写任务代码
    val lineRDD = sc.makeRDD(List(1, 2, 3, 4, 5))

    lineRDD.saveAsTextFile("output2")
    //此处没有给分区个数，会按照Mac机器core个数分区--->8

    //4.关闭SparkContext
    sc.stop()
  }
}
