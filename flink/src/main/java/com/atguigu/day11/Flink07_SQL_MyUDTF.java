package com.atguigu.day11;

import com.atguigu.day11.function.MyUDF;
import com.atguigu.day11.function.MyUDTF;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author ahao
 * @date 2022/7/14 19:12
 */
public class Flink07_SQL_MyUDTF {
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
        tableEnv.createTemporaryFunction("my_udtf", MyUDTF.class);

        /*//TODO 4.使用udf做查询并打印
        tableEnv.sqlQuery("" +
                        "SELECT   " +
                        "   id,   " +
                        "   word    " +
                        "FROM sensor_pt,   " +
                        "LATERAL TABLE(my_udtf(id))")
                        .execute()
                        .print();*/

        //TODO 4.使用udf做查询并打印
        //对炸裂后的字段重命名AS T(newWord, newLength) ON TRUE      T：表名
        //要用LEFT JOIN，用LEFT JOIN后，from 表名后面的 , 要去除，格式较为死板
        tableEnv.sqlQuery("" +
                        "SELECT   " +
                        "   id,   " +
                        "   newWord,    " +
                        "   newLength    " +
                        "FROM sensor_pt   " +
                        "LEFT JOIN LATERAL TABLE(my_udtf(id)) AS T(newWord, newLength) ON TRUE")
                .execute()
                .print();
    }
}
