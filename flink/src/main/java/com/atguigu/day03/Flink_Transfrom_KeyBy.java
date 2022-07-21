package com.atguigu.day03;

import com.atguigu.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ahao
 * @date 2022/7/2 18:56
 */
public class Flink_Transfrom_KeyBy {
    public static void main(String[] args) throws Exception {
        //获取流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //并行度
        env.setParallelism(4);

        //从端口获取数据
        DataStreamSource<String> streamSource = env.socketTextStream("hadoop102", 9999);

        //使用map将从端口读出来的数据转换成watersensor
        SingleOutputStreamOperator<WaterSensor> map = streamSource.map(new MapFunction<String, WaterSensor>() {
            @Override
            public WaterSensor map(String value) throws Exception {
                String[] split = value.split(",");
                return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
            }
        }).setParallelism(2);

        //使用keyby将相同的ID聚合到一起
        KeyedStream<WaterSensor, Tuple> keyedStream = map.keyBy("id");

        /**
         * map 和 map.print 并行度都为2，OneToOne,会窜在一起
         * keyedStream.print并行度为4，测试看从map，怎么发往keyedStream.print
         *
         * 结果：相同key的数据会发往相同分区，一个分区可能有多个key (在不同的分组）
         */

        map.print("原始分区").setParallelism(2);

        keyedStream.print("keyBy");


        env.execute();
    }
}
