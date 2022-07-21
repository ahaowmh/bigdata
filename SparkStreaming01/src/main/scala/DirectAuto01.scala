import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author ahao
 * @date 2022/5/20 19:44
 */
object DirectAuto01 {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //定义kafka参数，kafka集群地址 消费者组名称 key序列化 value序列化
    val kafkaParams = Map[String,Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop102:9092,hadoop103:9092,hadoop104:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )

    //ssc连接kafka.接收数据，创建DStream
    val kafkaDStream = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set("testTopic01"), kafkaParams)
    )

    //从kafkaDStream返回的记录中获取value值,并进行wc计算
    kafkaDStream.map(_.value()).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
