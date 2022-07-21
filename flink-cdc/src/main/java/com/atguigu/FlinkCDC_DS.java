package com.atguigu;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ahao
 * @date 2022/7/15 10:08
 */
public class FlinkCDC_DS {

    public static void main(String[] args) throws Exception {

        //TODO 1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //开启Checkpoint
        //每间隔5秒做一次，并设置一致性语义
        env.enableCheckpointing(5000L, CheckpointingMode.EXACTLY_ONCE);
        //设置超时时间为1分钟
        env.getCheckpointConfig().setCheckpointTimeout(10000L);
        //设置端后状态
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("hdfs://hadoop102:8020/cdc/ck");


        //TODO 2.创建flinkcdcSource，从MySQL读取数据
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("hadoop102")
                .port(3306)
                .databaseList("gmall220212")
                .tableList("gmall220212.order_info")
                .username("root")
                .password("09061954")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .startupOptions(StartupOptions.initial())
                .build();

        DataStreamSource<String> mysqlDS = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MysqlSource");

        //TODO 3.查询打印
        mysqlDS.print(">>>>>>>>");

        //TODO 4.执行
        env.execute();

    }
}
