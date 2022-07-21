import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author ahao
 * @date 2022/5/20 12:05
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    //监控端口创建DStream,设置receiver读进来的数据为一行一行的
    val lineDStream = ssc.socketTextStream("hadoop102", 9999)

    //将读进来的数据切分为单词
    val wordDStream = lineDStream.flatMap(_.split(" "))

    //转换元组数据结构
    val wordToOne = wordDStream.map((_, 1))

    //统计单词个数
    val wordToSum = wordToOne.reduceByKey(_ + _)

    //打印结果
    wordToSum.print()

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
