package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSQL05_UDF {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    //创建df
    val df = spark.read.json("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.json")
    //创建临时视图
    df.createOrReplaceTempView("user")
    //自定义udf
    spark.udf.register("addAge", (age: Long) => {
      age + 1
    })
    //SQL
    spark.sql("select name,addAge(age) from user").show()
    // 4 释放资源
    spark.stop()
  }
}
