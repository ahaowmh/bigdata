package creatrdd

import org.apache.spark.{SparkConf, SparkContext}

object CreatRDD_List {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    //若使用yarn-client模式 master可以填local[*]
    //若使用yarn-cluster模式 master位置留空
    val conf = new SparkConf().setAppName("SparkCore").setMaster("local[*]")

    //2.创建SparkContext
    val sc = new SparkContext(conf)

    //3.编写任务代码
    val list = List(1, 2, 3, 4, 5)

    //从集合中创建RDD
    //方式1.使用parallelize()
    val intRDD = sc.parallelize(list)
    //方式2.使用makeRDD(),底层调用的还是parallelize()
    val intRDD1 = sc.makeRDD(list)

    intRDD.collect().foreach(println)
    intRDD1.collect().foreach(println)

    //4.关闭SparkContext
    sc.stop()
  }
}
