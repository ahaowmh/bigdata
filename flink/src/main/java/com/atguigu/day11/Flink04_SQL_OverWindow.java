package com.atguigu.day11;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author ahao
 * @date 2022/7/14 11:09
 */
public class Flink04_SQL_OverWindow {
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

        //基于事件时间
        tableEnv.executeSql("" +
                "CREATE TABLE sensor_rt ( " +
                "  `id` STRING, " +
                "  `ts` BIGINT, " +
                "  `vc` DOUBLE, " +
                "  `rt` AS TO_TIMESTAMP_LTZ(ts,0), " +
                "  WATERMARK FOR rt AS rt - INTERVAL '5' SECOND " +
                ") WITH ( " +
                "  'connector' = 'kafka', " +
                "  'topic' = 'test', " +
                "  'properties.bootstrap.servers' = 'hadoop102:9092', " +
                "  'properties.group.id' = 'bigdata_0212', " +
                "  'scan.startup.mode' = 'latest-offset', " +
                "  'format' = 'csv' " +
                ")");

        //TODO 3.按照ID分组，ts开窗，vc聚合  ROWS BETWEEN 2 PRECEDING AND CURRENT ROW 当前行往前两行（共三行
        //处理时间（一般常用）
        tableEnv.sqlQuery("" +
                "select  " +
                "   id,  " +
                "   sum(vc) over(partition by id order by pt ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) sum_vc  " +
                "from sensor_pt");

        //事件时间
        tableEnv.sqlQuery("" +
                "select  " +
                "   id,  " +
                "   sum(vc) over(partition by id order by rt ROWS BETWEEN 2 PRECEDING AND CURRENT ROW) sum_vc  " +
                "from sensor_rt").execute().print();
    }
}
