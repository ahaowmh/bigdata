package com.atguigu.handler

import com.atguigu.bean.StartUpLog
import org.apache.spark.SparkContext
import org.apache.spark.streaming.dstream.DStream
import redis.clients.jedis.Jedis

import java.lang
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author ahao
 * @date 2022/6/22 11:02
 */


object DauHandler {
  /**
   * 批次内去重：1.将批次间去重后的数据按照相同日期+相同mid做聚合（groupByKey）
   * -->2.按照时间戳有小到大排序（sort）
   * -->3.取第一条（take）
   * @param filterByRedisDStream
   */
  def filterByGroup(filterByRedisDStream: DStream[StartUpLog]) = {
    //0.groupByKey针对数据类型是kv,所以将原数据转变成kv类型。K：mid+log.logDate,V:StartUpLog
    val midWithLogDateToLogDStream = filterByRedisDStream.map(log => {
      ((log.mid, log.logDate), log)
    })
    //1.聚合. 会将相同key的数据放到一个迭代器中
    val midWithLogDateToIterDStream = midWithLogDateToLogDStream.groupByKey()

    //2.迭代器中没有sort算子，将迭代器转换为list集合-->sort-->take(1)
    val midWithLogDateToListLogDStream = midWithLogDateToIterDStream.mapValues(iter => {
      iter.toList.sortWith(_.ts<_.ts).take(1)
    })
    //3.将list集合中的数据打散
    val value = midWithLogDateToListLogDStream.flatMap(_._2)
    value
  }


  /**
   * 批次间去重
   *
   * @param startUpLogDStream
   */
  def filterByRedis(startUpLogDStream: DStream[StartUpLog], sc: SparkContext) = {
    /*//使用filter算子将当前mid和Redis中保存的mid做对比，过滤掉重复数据
    val value = startUpLogDStream.filter(log => {
      //创建Redis连接
      val jedis = new Jedis("hadoop102", 6379)

      //查询Redis中mid是否与当前mid重复
      val redisKey: String = "Dau:" + log.logDate
      //sismember方法，判断log.mid(第二个参数）是否存在于redisKey（第一个参数），存在返回TRUE，否则FALSE
      val boolean: lang.Boolean = jedis.sismember(redisKey, log.mid)

      jedis.close()
      //filter算子，返回值为TRUE则保留，FALSE则过滤掉
      //因此，将sismember返回值取反，作为filter的过滤条件
      !boolean
    })
    value*/

    /* //第二版；在每个分区内创建Redis连接
     //可以创建分区的算子：mappartition(有返回值)和foreachpartition(无返回值）
     val value = startUpLogDStream.mapPartitions(partition => {
       //创建Redis连接
       val jedis = new Jedis("hadoop102", 6379)
       val logs = partition.filter(log => {
         //查询Redis中mid是否与当前mid重复
         val redisKey: String = "Dau:" + log.logDate
         //sismember方法，判断log.mid(第二个参数）是否存在于redisKey（第一个参数），存在返回TRUE，否则FALSE
         val boolean: lang.Boolean = jedis.sismember(redisKey, log.mid)
         !boolean
       })
       jedis.close()
       logs
     })*/

    //第三版；在每个rdd中创建Redis连接
    //可以创建rdd的算子：foreachRDD(没有返回值)和transform(有返回值)
    val value = startUpLogDStream.transform(rdd => {
      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      //在每个批次下创建Redis连接，此连接是在Driver端创建，连接也不能序列化
      //而当前数据是在Executor端获取-----》在Driver端查询Redis中保存的数据，通过广播发送到Executor端 做数据去重
      val jedis = new Jedis("hadoop102", 6379)

      //在Driver端查询Redis中保存的数据
      //log.logDate在Executor端获取，获取不到，所以用当前系统时间来format
      val time = sdf.format(new Date(System.currentTimeMillis()))
      val redisKey = "Dau:" + time
      val mids = jedis.smembers(redisKey)

      //将查询到的set集合广播到Executor端
      val midsBc = sc.broadcast(mids)

      //遍历rdd中的每条数据做去重
      val logs = rdd.filter(log => {
        //获取广播过来的数据
        val bool = midsBc.value.contains(log.mid)
        !bool
      })
      jedis.close()
      logs
    })
    value
  }




  /**
   * 将最终去重的mid写入Redis
   * @param startUpLogDStream
   */
  def saveMidToRedis(startUpLogDStream: DStream[StartUpLog]) = {
    //写入数据，不需要返回值，调用foreachPartition或者foreachRDD（只提供了这个）
    startUpLogDStream.foreachRDD(rdd =>{
      //如果在此创建Redis连接，为每个rdd创建一次连接，比在每个分区创建连接数量更少 更好
      //但是此处foreachRDD代码在Diver端执行，写入数据的foreachPartition代码在Executor端执行。
      //连接不能序列化。所以写入数据需要与Redis建立的连接必须在Executor端

      rdd.foreachPartition(partition=>{
        //在分区内创建Redis连接
        val jedis = new Jedis("hadoop102", 6379)

        partition.foreach(log=>{
          //将mid写入Redis
          val redisKey: String = "Dau:" + log.logDate
          jedis.sadd(redisKey,log.mid)
        })
        //为每个分区关闭Redis连接
        jedis.close()
      })
    })
  }


}
