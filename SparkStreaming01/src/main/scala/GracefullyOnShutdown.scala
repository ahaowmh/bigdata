import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

import java.net.URI


/**
 * @author ahao
 * @date 2022/5/20 21:07
 */
object GracefullyOnShutdown {
  def main(args: Array[String]): Unit = {
    //初始化Spark配置信息
    val sparkConf = new SparkConf().setAppName("SparkStreaming").setMaster("local[*]")

    // 设置优雅的关闭
    sparkConf.set("spark.streaming.stopGracefullyOnShutdown", "true")

    //初始化SparkStreamingContext,第二个参数batchDuration，批次持续时间
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val lineDStream = ssc.socketTextStream("hadoop102", 9999)

    lineDStream.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    //开启监控线程
    new Thread(new MonitorStop(ssc)).start()

    //启动SparkStreamingContext,并将主线程阻塞
    ssc.start()
    ssc.awaitTermination()
  }
}
//监控程序
class MonitorStop(ssc: StreamingContext) extends Runnable {
  override def run(): Unit = {
    val fs: FileSystem = FileSystem.get(new URI("hadoop102:8020"),new Configuration(),"atguigu")


    while (true){
      //获取stopspark路径是否存在
      val result = fs.exists(new Path("hdfs://hadoop102:8020/stopSpark"))

      if(result){
        val state: StreamingContextState = ssc.getState()
        // 获取当前任务是否正在运行
        if (state == StreamingContextState.ACTIVE){
          // 优雅关闭
          ssc.stop(stopSparkContext = true, stopGracefully = true)
          System.exit(0)
        }
      }
    }
  }
}