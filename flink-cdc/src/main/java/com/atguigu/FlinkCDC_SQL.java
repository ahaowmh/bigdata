package com.atguigu;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableAggregateFunction;

/**
 * @author ahao
 * @date 2022/7/15 17:07
 */
public class FlinkCDC_SQL {
    public static void main(String[] args) {

        //TODO 1.获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //TODO 2.使用DDL方式建表
        //格式模仿。
        tableEnv.executeSql("" +
                "CREATE TABLE base_trademark ( " +
                " id INT NOT NULL, " +
                " tm_name STRING, " +
                " logo_url STRING " +
                ") WITH ( " +
                " 'connector' = 'mysql-cdc', " +
                " 'hostname' = 'hadoop102', " +
                " 'port' = '3306', " +
                " 'username' = 'root', " +
                " 'password' = '000000', " +
                " 'database-name' = 'gmall-211227-flink', " +
                " 'table-name' = 'base_trademark', " +
                " 'scan.incremental.snapshot.enabled' = 'false' " +
                ")");
        //缺少主键报错：解决：1.添加一个字段为主键   2.scan.incremental.snapshot.enabled' = 'false

        //TODO 3.查询并打印
        tableEnv.sqlQuery("select * from base_trademark")
                .execute()
                .print();

    }
}
