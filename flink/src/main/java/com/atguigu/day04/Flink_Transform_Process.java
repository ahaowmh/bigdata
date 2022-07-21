package com.atguigu.day04;

import com.atguigu.bean.WaterSensor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;

/**
 * @author ahao
 * @date 2022/7/4 11:29
 */
public class Flink_Transform_Process {
    public static void main(String[] args) throws Exception {
        //创建流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度为1，方便测试查看结果
        env.setParallelism(1);

        //从端口获取数据
        DataStreamSource<String> streamSource = env.socketTextStream("hadoop102", 9999);

        //将获取的数据用process算子转换为watersensor  （和map算子作用相同）process算子属于最底层的API，可以实现flink的所有功能
        SingleOutputStreamOperator<WaterSensor> map = streamSource.process(new ProcessFunction<String, WaterSensor>() {
            @Override
            public void processElement(String value, ProcessFunction<String, WaterSensor>.Context ctx, Collector<WaterSensor> out) throws Exception {
                String[] split = value.split(",");
                out.collect(new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2])));
            }
        });

        //使用keyby将相同ID的数据聚合到一起
        KeyedStream<WaterSensor, Tuple> keyBy = map.keyBy("id");


        /**
         * bug:每一个数据都共用一个lastSumVc，不能根据不同的ID做sum
         */
//        //使用process来实现sum的功能
//        SingleOutputStreamOperator<WaterSensor> process = keyBy.process(new KeyedProcessFunction<Tuple, WaterSensor, WaterSensor>() {
//
//            //定义一个累加器，用来保存上一次的累计结果
//            private Integer lastSumVc = 0;
//
//            @Override
//            public void processElement(WaterSensor value, KeyedProcessFunction<Tuple, WaterSensor, WaterSensor>.Context ctx, Collector<WaterSensor> out) throws Exception {
//                lastSumVc += value.getVc();
//                //用迭代器out将结果返回给下游
//                out.collect(new WaterSensor(value.getId(), value.getTs(), lastSumVc));
//            }
//        });


        SingleOutputStreamOperator<WaterSensor> process = keyBy.process(new KeyedProcessFunction<Tuple, WaterSensor, WaterSensor>() {

            //定义一个hashmap,存放获取到的ID和VC合
            private HashMap<String, Integer> lastSumMap = new HashMap<>();

            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<Tuple, WaterSensor, WaterSensor>.Context ctx, Collector<WaterSensor> out) throws Exception {
                //判断当前数据的ID是否是第一次获取
                if (lastSumMap.containsKey(value.getId())) {
                    //根据获取数据的ID去lastSumMap中获取ID对应的VC累加值
                    Integer lastSumVc = lastSumMap.get(value.getId());
                    //累加VC
                    lastSumVc += value.getVc();
                    //将累加后的值更新到lastSumMap中
                    lastSumMap.put(value.getId(), lastSumVc);
                    //将结果返回
                    out.collect(new WaterSensor(value.getId(), value.getTs(), lastSumVc));
                } else {
                    //此ID不在lastSumMap中，直接将获取到的VC放到lastSumMap中，并输出
                    lastSumMap.put(value.getId(), value.getVc());
                    out.collect(value);
                }
            }
        });

        process.print();

        env.execute();
    }
}
