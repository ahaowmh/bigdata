package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object MySQL_Read {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    //从hadoop102的MySQL的gmall库中读取表user_info
    val df = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/gmall")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "09061954")
      .option("dbtable", "user_info")
      .load()
    //默认只show20行，截断显示
    df.show(500,false)

    //创建视图，执行SQL
    df.createTempView("user")
    spark.sql("select user_level,count(*) from user group by user_level").show()

    // 4 释放资源
    spark.stop()
  }
}
