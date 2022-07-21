package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSQL03_RDDAndDataSet {
  def main(args: Array[String]): Unit = {
    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 3 编写逻辑代码
    // 创建sc对象
    val sc = spark.sparkContext
    // 创建RDD
    val lineRDD = sc.textFile("/Users/ahao/Documents/IdeaProjects/SparkSQL/input/user.txt")
    //普通rdd,数据只有类型,没有列名(缺少元数据).转换数据结构  qiaofeng,20 => (qiaofeng,20)
    val rdd = lineRDD.map {
      line => {
        //以行读取，经过split，转变为一个个单独的字段
        val fileds = line.split(",")
        //将每一行的两个字段变为二元组形式
        (fileds(0), fileds(1))
      }
    }
    //转换样例类RDD，有类型,有属性名(列名),不缺元数据
    val userRDD = rdd.map {
      case (name, age) => User(name, age.toLong)
    }
    rdd.collect().foreach(println)
    userRDD.collect().foreach(println)
    println("rdd=================")

    //RDD和DF、DS转换必须要导的包(隐式转换)
    import spark.implicits._

    //RDD => DS  --->.toDS
    //普通rdd转ds,不能手动补充列名.so,最好用样例类转
    val ds = rdd.toDS()
    val userDS = userRDD.toDS()
    ds.show()
    userDS.show()
    println("ds=================")

    //DS => RDD  --->.rdd
    val rdd1 = ds.rdd
    val userRDD1 = userDS.rdd
    rdd1.collect().foreach(println)//(qiaofeng,20)
    userRDD1.collect().foreach(println)//User(qiaofeng,20)
    println("rdd=================")


    // 4 释放资源
    spark.stop()
  }
}
