package com.atguigu.day10;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


/**
 * @author ahao
 * @date 2022/7/12 23:04
 */
public class K2K {
    public static void main(String[] args) throws Exception {

        //TODO 1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //TODO 2.使用DDL方式创建KafkaSource
        tableEnv.executeSql("" +
                "CREATE TABLE sensor1 ( " +
                "  `id` STRING, " +
                "  `ts` BIGINT, " +
                "  `vc` DOUBLE " +
                ") WITH ( " +
                "  'connector' = 'kafka', " +
                "  'topic' = 'test', " +
                "  'properties.bootstrap.servers' = 'hadoop102:9092', " +
                "  'properties.group.id' = 'bigdata_0212', " +
                "  'scan.startup.mode' = 'latest-offset', " +
                "  'format' = 'csv' " +
                ")");

        //TODO 3.读取Kafka数据转换为流做打印
        Table resultTable = tableEnv.sqlQuery("select * from sensor1 where id>='1002'");
        tableEnv.toDataStream(resultTable).print(">>>>>>>>>");
        tableEnv.createTemporaryView("result_table", resultTable);

        //TODO 4.使用DDL方式创建KafkaSink
        tableEnv.executeSql("" +
                "CREATE TABLE sensor2 ( " +
                "  `id` STRING, " +
                "  `ts` BIGINT, " +
                "  `vc` DOUBLE " +
                ") WITH ( " +
                "  'connector' = 'kafka', " +
                "  'topic' = 'test1', " +
                "  'properties.bootstrap.servers' = 'hadoop102:9092', " +
                "  'format' = 'json' " +
                ")");

        //TODO 5.将数据写出
        tableEnv.executeSql("insert into sensor2 select * from result_table");

        //TODO 6.启动任务
        env.execute();
    }
}
