package creatrdd

import org.apache.spark.{SparkConf, SparkContext}

object CreatRDD_file {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    //若使用yarn-client模式 master可以填local[*]
    //若使用yarn-cluster模式 master位置留空
    val conf = new SparkConf().setAppName("SparkCore").setMaster("local[*]")

    //2.创建SparkContext
    val sc = new SparkContext(conf)

    //3.编写任务代码
    //从文件中读取数据，不管文件中是什么数据类型，都做字符串处理
    //可以在textFile()添加第二个参数，控制分区数
    val lineRDD = sc.textFile("input/1.txt",10)

    //lineRDD.collect().foreach(println)
    //将RDD保存到文件，有几个分区，就有几个文件生成
    lineRDD.saveAsTextFile("output")
    //4.关闭SparkContext
    sc.stop()
  }
}
