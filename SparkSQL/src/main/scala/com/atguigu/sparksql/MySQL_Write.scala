package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object MySQL_Write {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    //创建df
    val df = spark.read.json("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.json")

    df.show()
    //当df的列名与要写入的表的列名不一致时，可以先将df转换为一致的列名
    val df2 = df.toDF("age2", "name2")

    //写入数据到mysql
    df2.write.format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/test")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "user4")
      .mode(SaveMode.Append)
      .save()

    // 4 释放资源
    spark.stop()
  }
}
