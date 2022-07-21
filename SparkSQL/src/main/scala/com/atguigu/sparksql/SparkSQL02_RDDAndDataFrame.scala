package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


object SparkSQL02_RDDAndDataFrame {
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
//    val userRDD = rdd.map {
//      t => {
//        User(t._1, t._2.toLong)
//      }
//    }

    rdd.collect().foreach(println)//(qiaofeng,20)
    userRDD.collect().foreach(println)//User(qiaofeng,20)
    println("=================")
    
    //RDD和DF、DS转换必须要导的包(隐式转换)
    import spark.implicits._

    //RDD => DF   --->.toDF
    //普通RDD转换DF需要手动为每列加上列名(补充元数据）
    val df = rdd.toDF("name","age")
    //样例类RDD不缺元数据
    val userDF = userRDD.toDF()

    df.show()
    userDF.show()
    println("=================")

    //DF => RDD    --->.rdd
    //DF不管数据类型，所有数据类型在DF都为Row
    val rdd1 = df.rdd
    val userRDD1 = userDF.rdd
    rdd1.collect().foreach(println)//[qiaofeng,20]
    userRDD1.collect().foreach(println)//[qiaofeng,20]

    //将Row中的数据读出来，变成user
    val userRDD2 = userRDD1.map {
      row => User(row.getString(0), row.getLong(1))
    }
    userRDD2.collect().foreach(println)//User(qiaofeng,20)

    // 4 释放资源
    spark.stop()
  }
}

case class User(name: String, age: Long)