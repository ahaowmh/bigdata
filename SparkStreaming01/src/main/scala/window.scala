import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author ahao
 * @date 2022/5/20 20:47
 */
object window {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lineDStream = ssc.socketTextStream("hadoop102", 9999)
    val wordToOne = lineDStream.flatMap(_.split(" ")).map((_, 1))
    /*窗口时长:计算内容的时间范围
    滑动步长:隔多久触发一次计算
    两者都必须为采集批次大小的整数倍*/
    //获取窗口返回数据 --> 聚合计算 --> 打印
    wordToOne.window(Seconds(12),Seconds(6)).reduceByKey(_+_).print()
    //如果有多批数据进入窗口，最终也会通过window操作变成统一的RDD处理。

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
