package com.atguigu.day11;

import com.atguigu.day11.function.MyUDF;
import com.atguigu.day11.function.MyUDTAF;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

/**
 * @author ahao
 * @date 2022/7/14 19:12
 */
public class Flink09_SQL_MyUDTAF {
    public static void main(String[] args) {

        //TODO 1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //TODO 2.使用DDL方式从kafka读取数据创建动态表。并提取处理时间和事件时间
        //基于处理时间
        tableEnv.executeSql("" +
                "CREATE TABLE sensor_pt ( " +
                "  `id` STRING, " +
                "  `ts` BIGINT, " +
                "  `vc` DOUBLE, " +
                "  `pt` AS PROCTIME() " +
                ") WITH ( " +
                "  'connector' = 'kafka', " +
                "  'topic' = 'test', " +
                "  'properties.bootstrap.servers' = 'hadoop102:9092', " +
                "  'properties.group.id' = 'bigdata_0212', " +
                "  'scan.startup.mode' = 'latest-offset', " +
                "  'format' = 'csv' " +
                ")");

        //TODO 3.注册UDF
        tableEnv.createTemporaryFunction("my_udtaf", MyUDTAF.class);

        //TODO 4.使用udtaf做查询并打印
        //只能使用tableAPI
        tableEnv.from("sensor_pt")
                .groupBy($("id"))
                .flatAggregate(call("my_udtaf",$("vc")).as("v","rk"))
                .select($("id"),$("v"),$("rk"))
                .execute().print();

    }
}
