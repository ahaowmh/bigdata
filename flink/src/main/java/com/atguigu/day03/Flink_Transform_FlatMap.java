package com.atguigu.day03;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author ahao
 * @date 2022/7/2 20:16
 */
public class Flink_Transform_FlatMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        DataStreamSource<String> streamSource = env.socketTextStream("hadoop102", 9999);

        //用flatmap将读过来的数据切分为一个个单词
        streamSource.flatMap(new FlatMapFunction<String, String>() {

            /**
             * flatmap 一进多出函数。
             * @param value
             * @param out 利用collect将多个结果返回
             * @throws Exception
             */
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] words = value.split(" ");
                for (String word : words) {
                    out.collect(word);
                }
            }
        }).print();

        env.execute();
    }

}
