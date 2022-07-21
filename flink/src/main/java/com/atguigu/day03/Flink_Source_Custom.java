package com.atguigu.day03;

import com.atguigu.bean.WaterSensor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * @author ahao
 * @date 2022/7/2 15:49
 */
public class Flink_Source_Custom {
    public static void main(String[] args) throws Exception {
        //获取流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度为1
        env.setParallelism(1);

        //通过自定义source来获取数据
        env.addSource(new MySource()).print();

        env.execute();

    }
    //自定义source随机生成watersensor数据
    //如果自定义source想要设置并行度，可以继承ParallelSourceFunction(继承了SourceFunction）
    public static class MySource implements SourceFunction<WaterSensor>{
    //public static class MySource implements ParallelSourceFunction<WaterSensor>{
        //随机数
        private Random random = new Random();
        //控制生成数据循环
        private Boolean isRunning = true;

        @Override
        public void run(SourceContext<WaterSensor> ctx) throws Exception {
            while (isRunning){
                ctx.collect(new WaterSensor("sensor" + random.nextInt(1000),System.currentTimeMillis(),random.nextInt(100)));

                //设置线程睡眠
                Thread.sleep(100);
            }
        }

        //在cancel中控制生成数据循环
        @Override
        public void cancel() {
            isRunning = false;
        }
    }
}
