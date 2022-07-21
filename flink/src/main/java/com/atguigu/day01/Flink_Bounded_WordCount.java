package com.atguigu.day01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author ahao
 * @date 2022/6/30 09:09
 */
public class Flink_Bounded_WordCount {
    public static void main(String[] args) throws Exception {
        //获取流处理的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //测试，并行度设置为1
        env.setParallelism(1);

        //从文件中读取数据
        DataStreamSource<String> lines = env.readTextFile("input/words.txt");

        //将文件中每行数据按照空格切分为单个单词
        SingleOutputStreamOperator<String> wordDStream = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                //按照空格切分
                String[] words = s.split(" ");
                //遍历每个单词
                for (String word : words) {
                    collector.collect(word);
                }
            }
        });

        //将每一个单词组成tuple二元组
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordToOneDStream = wordDStream.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return Tuple2.of(s, 1);
            }
        });

        //将相同单词聚合到一起
        KeyedStream<Tuple2<String, Integer>, String> keyedStream = wordToOneDStream.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                return stringIntegerTuple2.f0;
            }
        });

        //做累加运算
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = keyedStream.sum(1);

        //打印输出
        result.print();

        //触发执行操作。相当于提交了一个job
        env.execute();
    }
}
