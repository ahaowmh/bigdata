package com.atguigu.day03;

import com.atguigu.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ahao
 * @date 2022/7/2 18:56
 */
public class Flink_Transfrom_Map {
    public static void main(String[] args) throws Exception {
        //获取流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //并行度
        env.setParallelism(1);

        //从端口获取数据
        DataStreamSource<String> streamSource = env.socketTextStream("hadoop102", 9999);

        //使用map将从端口读出来的数据转换成watersensor
//        SingleOutputStreamOperator<WaterSensor> map = streamSource.map(new MapFunction<String, WaterSensor>() {
//            @Override
//            public WaterSensor map(String value) throws Exception {
//                String[] split = value.split(",");
//                return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
//            }

        SingleOutputStreamOperator<WaterSensor> map = streamSource.map(new MyMap());

        map.print();
        env.execute();
    }
    //富函数
    public static class MyMap extends RichMapFunction<String,WaterSensor>{
        //ctl + o 实现或者重写父类的方法

        /**
         * 声明周期方法，程序开始的标志，每个并行线程调用一次（与并行度的设置有关）
         * 适用于初始化操作，如建立链接
         * @param parameters
         * @throws Exception
         */
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
        }

        /**
         * 声明周期方法，程序结束的标志，每个并行线程调用一次。。特殊：从文件读数据，每个并行度调用两次
         * 适用于收尾工作，如释放资源，关闭链接
         * @throws Exception
         */
        @Override
        public void close() throws Exception {
            super.close();
        }

        @Override
        public WaterSensor map(String value) throws Exception {
            //getRuntimeContext()方法获取上下文
            System.out.println(getRuntimeContext().getJobId());
            String[] split = value.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        }
    }

}
