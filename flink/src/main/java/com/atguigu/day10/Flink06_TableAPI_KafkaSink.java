package com.atguigu.day10;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Json;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author ahao
 * @date 2022/7/12 22:54
 */
public class Flink06_TableAPI_KafkaSink {
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

        //TODO 3. 执行查询并写到kafka
        Table sensorTable = tableEnv.from("sensor");
        Table whereTable = sensorTable.where($("id").isGreaterOrEqual("sensor_2"));

        tableEnv.connect(new Kafka()
                        .version("universal")
                        .topic("test1")
                        .property(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092"))
                .withFormat(new Json())
                .withSchema(schema)
                .createTemporaryTable("sensor2");

        whereTable.executeInsert("sensor2");
    }
}
