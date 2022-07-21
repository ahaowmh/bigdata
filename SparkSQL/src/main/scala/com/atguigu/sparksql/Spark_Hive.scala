package com.atguigu.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Spark_Hive {
  def main(args: Array[String]): Unit = {
    //配置去HDFS的用户名  我的Mac用户名和Hadoop用户名一致，可不配置
    //System.setProperty("HADOOP_USER_NAME","ahao")

    // 1 创建上下文环境配置对象
    val conf: SparkConf = new SparkConf().setAppName("SparkSQLTest").setMaster("local[*]")

    // 2 创建SparkSession对象
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate()

    // 3 编写逻辑代码
    /**
     * idea写sparkSQL连接外部hive
     * 1、导入pom依赖，spark-sql  spark-hive  mysql连接驱动
     * 2、将hive-site.xml 复制一份到项目的类路径下
     * 3、在代码里面  获取外部Hive的支持  .enableHiveSupport()
     * 4、若仍然报错，将core-site.xml  hdfs_site.xml yarn_site.xml 拷贝一份到类路径下（Hadoop本地环境）
     *
     * 5、因为外部hive的数据表存在HDFS上，所以要配置进入Hadoop的用户名
     */
    //连接外部hive
    spark.sql("show tables").show()

    spark.sql("select * from test").show()



    // 4 释放资源
    spark.stop()
  }
}
