package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSQL04_DataFrameAndDataSet {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    //创建DF
    val df = spark.read.json("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.json")

    import spark.implicits._
    //DF => DS  --->.as[]
    val ds = df.as[User]
    ds.show()

    //DS => DF  --->.toDF
    val df1 = ds.toDF()
    df1.show()


    // 4 释放资源
    spark.stop()
  }
}
