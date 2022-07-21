package com.atguigu.day02;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

/**
 * @author ahao
 * @date 2022/7/1 21:53
 */
public class Flink01_Source_Collection {
    public static void main(String[] args) throws Exception {
        //获取流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度为1，方便测试查看结果
        env.setParallelism(1);

//        //从集合中获取数据
//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
//        env.fromCollection(list).print();

//        //从元素中获取数据，测试用 更方便
//        env.fromElements(1,2,3,4,5,6,7).print();

        env.readTextFile("hdfs://hadoop102:8020/111.txt").print();

        env.execute();
    }
}
