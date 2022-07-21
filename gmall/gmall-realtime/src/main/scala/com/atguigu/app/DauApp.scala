package com.atguigu.app

import com.alibaba.fastjson.JSON
import com.atguigu.bean.StartUpLog
import com.atguigu.constants.GmallConstants
import com.atguigu.handler.DauHandler
import com.atguigu.utils.MyKafkaUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.phoenix.spark._

import java.text.SimpleDateFormat
import java.util.Date


/**
 * @author ahao
 * @date 2022/6/21 19:24
 */
object DauApp {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf
    val sparkConf = new SparkConf().setAppName("DauApp").setMaster("local[*]")

    //2.创建ssc(StreamingContext)
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(3))

    //3.通过kafka工具类消费启动日志数据
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(GmallConstants.KAFKA_TOPIC_STARTUP, ssc)

    //4.将消费到的josn字符串转换为样例类，为后续操作方便，补充logdate&longHour俩字段
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH")
    val startUpLogDStream: DStream[StartUpLog] = kafkaDStream.mapPartitions(partition => {
      partition.map(record => {
        val startUpLog: StartUpLog = JSON.parseObject(record.value(), classOf[StartUpLog])

        //补全俩字段
        val times: String = sdf.format(new Date(startUpLog.ts))

        startUpLog.logDate = times.split(" ")(0)
        startUpLog.logHour = times.split(" ")(1)

        startUpLog
      })
    })

    //startUpLogDStream 流用到两次，可将其缓存(小优化）
    startUpLogDStream.cache()

    //打印原始数据条数
    startUpLogDStream.count().print()

    //5.对相同日期、相同mid的数据做批次间去重
    val filterByRedisDStream = DauHandler.filterByRedis(startUpLogDStream,ssc.sparkContext)

    //数据缓存（小优化）
    filterByRedisDStream.cache()

    //打印批次间处理后的数据条数
    filterByRedisDStream.count().print()

    //6.对经过批次间去重后的数据做批次内去重filterByRedisDStream
    val filterByGroupDStream = DauHandler.filterByGroup(filterByRedisDStream)

    //数据缓存（小优化）
    filterByGroupDStream.cache()

    //打印批次nei处理后的数据条数
    filterByGroupDStream.count().print()

    //7.将最终去重后的mid写入Redis，方便后面批次间去重
    DauHandler.saveMidToRedis(filterByGroupDStream)

    //8.将最终去重后的明细数据写入HBASE
    filterByGroupDStream.foreachRDD(rdd => {
      rdd.saveToPhoenix( "GMALL220212_DAU",
        Seq("MID", "UID", "APPID", "AREA", "OS", "CH", "TYPE", "VS", "LOGDATE", "LOGHOUR", "TS"),
        HBaseConfiguration.create,
        Some("hadoop102,hadoop103,hadoop104:2181"))
    })

    //打印测试
    //startUpLogDStream.print()
    //启动并阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
