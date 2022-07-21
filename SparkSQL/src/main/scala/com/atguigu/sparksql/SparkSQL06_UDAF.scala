package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}
import org.apache.spark.sql.expressions.Aggregator

object SparkSQL06_UDAF {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    //创建df
    val df = spark.read.json("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.json")
    //创建临时视图
    df.createTempView("user")
    //注册udaf
    spark.udf.register("myAvg",functions.udaf(new MyAvgUDAF) )
    //SQL风格
    spark.sql("select myAvg(age) from user").show()

    // 4 释放资源
    spark.stop()
  }
}
//sum和cunt都需要变化，默认为val，不可
case class Buff(var sum:Long,var cnt:Long)
//创建样例类
case class MyAvgUDAF() extends Aggregator[Long,Buff,Double]{
  //缓冲区Buff的初始化方法
  override def zero: Buff = Buff(0L,0L)
  //缓冲区在单个分区内的一个聚合方法
  override def reduce(buff: Buff, age: Long): Buff = {
    buff.sum += age
    buff.cnt += 1
    buff
  }
  //缓冲区在多个分区间的一个合并方法
  override def merge(b1: Buff, b2: Buff): Buff = {
    b1.sum += b2.sum
    b1.cnt += b2.cnt
    b1
  }
  //最终的逻辑计算方法
  override def finish(reduction: Buff): Double = {
    reduction.sum.toDouble / reduction.cnt
  }
  //序列化方法
  override def bufferEncoder: Encoder[Buff] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
