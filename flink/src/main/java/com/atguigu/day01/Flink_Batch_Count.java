package com.atguigu.day01;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 批处理，基本不用，了解即可
 * @author ahao
 * @date 2022/6/29 22:09
 */
public class Flink_Batch_Count {
    public static void main(String[] args) throws Exception {
        //创建批处理执行环境
        ExecutionEnvironment evn = ExecutionEnvironment.getExecutionEnvironment();

        //获取文件中的数据
        DataSource<String> dataSource = evn.readTextFile("input/words.txt");

        //将文件中的每一行数据按照空格切分为单词
        //flatmap参数：1、自定义一个类，实现FlatMapFunction接口 2、匿名实现类 3、lambda（易报错）
        FlatMapOperator<String, String> value = dataSource.flatMap(new MyFlatMap());

        //将每一个单词组成tuple二元组
        MapOperator<String, Tuple2<String, Integer>> wordToOne = value.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                //return new Tuple2<>(s, 1);
                return Tuple2.of(s,1); //底层仍调用上面代码
            }
        });

        //将相同的单词聚合到一起。zero based index
        UnsortedGrouping<Tuple2<String, Integer>> groupby = wordToOne.groupBy(0);

        //对聚合在一起的单词做累加
        AggregateOperator<Tuple2<String, Integer>> result = groupby.sum(1);

        //打印结果
        result.print();
    }
    public static class MyFlatMap implements FlatMapFunction <String,String> {
        /**
         *
         * @param s 传入的数据
         * @param collector 采集器，可将数据发送到下游
         * @throws Exception
         */
        @Override
        public void flatMap(String s, Collector<String> collector) throws Exception {
            //将数据按照空格切分为单个单词
            String[] words = s.split(" ");
            //将切分的每个单词返回
            for (String word : words) {
                collector.collect(word);
            }
        }
    }
}

