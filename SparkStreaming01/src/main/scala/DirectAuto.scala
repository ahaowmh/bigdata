import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * @author ahao
 * @date 2022/5/20 13:04
 */
object DirectAuto {

  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //定义Kafka参数：kafka集群地址、消费者组名称、key序列化、value序列化
    val kafkaPara = Map[String,Object](
      //ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop102:9092,hadoop103:9092,hadoop104:9092"
      //public static final String BOOTSTRAP_SERVERS_CONFIG = "bootstrap.servers"
      "bootstrap.servers" -> "hadoop102:9092,hadoop103:9092,hadoop104:9092",
      "group.id" -> "atguiguGroup4",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      //从哪里继续读取
      "auto.offset.reset" -> "earliest",
      //是否手动维护offset 0-10 DirectAPI offset默认存储在：_consumer_offsets系统主题，手动维护：MySQL等有事务的存储系统
      //"enable.auto.commit" -> "true"
    )

    //ssc对接kafka, 读取数据，创建DStream
    val kafkaDStream = KafkaUtils.createDirectStream(
      ////提交job的入口
      ssc,
      //位置策略 it will consistently distribute partitions across all executors.
      LocationStrategies.PreferConsistent,
      //消费策略 订阅消费  set()去重集合
      ConsumerStrategies.Subscribe[String, String](Set("testTopic"), kafkaPara)
    )

    //kafkaDStream: InputDStream[ConsumerRecord[String, String]](kafka的每条记录）
    //取出kafkaDStream中的value
    val valueDStream = kafkaDStream.map(record => record.value())

    //wordcount计算
    valueDStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }


}
