package com.atguigu

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf配置对象
    val sparkConf = new SparkConf().setAppName("WC").setMaster("local[*]")
    //2.创建sc
    val sc = new SparkContext(sparkConf)
    //3.WordCount任务代码
    val lineRDD = sc.textFile("input")

    val wordRDD = lineRDD.flatMap(_.split(" "))

    val tupleRDD = wordRDD.map((_, 1))

    val resultRDD = tupleRDD.reduceByKey(_ + _)
    //调用行动算子才会计算
    val array = resultRDD.collect()

    array.foreach(println)

    //4.关闭sc
    sc.stop()
  }
}
