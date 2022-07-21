package com.atguigu.day11;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author ahao
 * @date 2022/7/14 11:09
 */
public class Flink05_SQL_NEW_TumbleWindow {
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

        //TODO 3.按照ID分组，ts开窗，vc聚合
        //新写法，方便后期优化
        tableEnv.sqlQuery("" +
                "select  " +
                "   id,  " +
                "   sum(vc),  " +
                "   window_start,  " +
                "   window_end  " +
                "from table(  " +
                "TUMBLE(TABLE sensor_pt, DESCRIPTOR(pt), INTERVAL '10' SECOND))  " +
                "group by id,window_start,window_end");

        tableEnv.sqlQuery("" +
                "select  " +
                "   id,  " +
                "   sum(vc),  " +
                "   window_start,  " +
                "   window_end  " +
                "from table(  " +
                "TUMBLE(TABLE sensor_rt, DESCRIPTOR(rt), INTERVAL '10' SECOND))  " +
                "group by id,window_start,window_end").execute().print();
    }
}
