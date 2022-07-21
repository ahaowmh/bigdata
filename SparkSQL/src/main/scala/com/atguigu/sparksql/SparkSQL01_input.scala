package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSQL01_input {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    // 读取数据
    val df = spark.read.json("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.json")

    // 可视化
    df.show()

    // 4 释放资源
    spark.stop()

  }
}


