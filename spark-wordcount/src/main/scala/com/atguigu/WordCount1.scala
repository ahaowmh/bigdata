package com.atguigu

import org.apache.spark.{SparkConf, SparkContext}

object WordCount1 {
  def main(args: Array[String]): Unit = {
    // 创建SparkCore配置对象
    val sparkConf = new SparkConf().setAppName("WC1").setMaster("local[*]")
    //创建sc
    val sc = new SparkContext(sparkConf)
    //编写任务代码
    //逐行读取
    val lineRDD = sc.textFile("input")
    //扁平化
    val wordRDD = lineRDD.flatMap(_.split(" "))
    //map
    val tupleRDD = wordRDD.map((_, 1))
    //reduce
    val resultRDD = tupleRDD.reduceByKey(_ + _)

    //行动算子
    resultRDD.saveAsTextFile("output")

    //关闭sc
    sc.stop()
  }
}
