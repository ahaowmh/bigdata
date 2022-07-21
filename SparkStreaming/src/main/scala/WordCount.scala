import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author ahao
 * @date 2022/5/19 16:23
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //通过监控端口来创建DStream
    val lineDStream = ssc.socketTextStream("hadoop102", 9999)

    //将获取的每行数据切分为一个个单词
    val wordDStream = lineDStream.flatMap(_.split(" "))

    //将单词转换为元组数据结构（word,1）
    val wordToOneDStream = wordDStream.map((_, 1))

    //按单词分组，统计个数
    val wordOneToSum = wordToOneDStream.reduceByKey(_ + _)

    //打印结果
    wordOneToSum.print()

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
