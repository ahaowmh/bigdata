/**
 * @author ahao
 * @date 2022/5/20 10:22
 */
object CustomerReceiver {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))



    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }

}
