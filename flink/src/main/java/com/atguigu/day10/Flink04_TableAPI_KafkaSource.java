package com.atguigu.day10;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author ahao
 * @date 2022/7/12 22:11
 */
public class Flink04_TableAPI_KafkaSource {

    public static void main(String[] args) {

        //TODO 1.获取流的执行环境、表的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //TODO 2.读取kafka数据创建表 设置读取数据格式 表结构
        Schema schema = new Schema()
                .field("id", DataTypes.STRING())
                .field("ts", DataTypes.BIGINT())
                .field("vc", DataTypes.INT());
        tableEnv.connect(new Kafka()
                .version("univerval")
                .topic("test")
                .startFromLatest()
                .property(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092")
                .property(ConsumerConfig.GROUP_ID_CONFIG,"bigdata_0212"))
                .withFormat(new Json())
                .withSchema(schema)
                .createTemporaryTable("sensor");

        //TODO 3. 执行查询并打印
        Table sensorTable = tableEnv.from("sensor");
        sensorTable.where($("id").isLessOrEqual("sensor_2"))
                .execute().print();

    }
}
