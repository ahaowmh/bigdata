package com.atguigu.day10;

import com.atguigu.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;


/**
 * @author ahao
 * @date 2022/7/12 12:32
 */
public class Flink01_TableToDS {
    public static void main(String[] args) throws Exception {
        //TODO 1.获取流的执行环境、表的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //TODO 2.从端口读取数据，创建流
        DataStreamSource<String> streamSource = env.socketTextStream("localhost", 9999);
        SingleOutputStreamOperator<WaterSensor> waterSensorDS = streamSource.map(line -> {
            String[] split = line.split(",");
            return new WaterSensor(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
        });

        //TODO 3.将流装换为动态表
        Table sensorTable = tableEnv.fromDataStream(waterSensorDS);

        //TODO 4.使用tableAPI简单查询
        Table selectTable = sensorTable
                .where($("id").isEqual("1001"))
                .groupBy($("id"))
                .aggregate($("vc").sum().as("vc"))
                .select($("id"), $("vc"));

        //TODO 5.将表转换位流进行打印
        /**
         * toDataStream和toAppendStream   追加流（不涉及状态，无法获取更新操作）
         * toChangelogStream和toChangelogStream  撤回流
         */
//        tableEnv.toDataStream(selectTable).print("toDataStream>>>>>>");
//        tableEnv.toAppendStream(selectTable,Row.class).print("toAppendStream>>>>>>>>>");
        tableEnv.toChangelogStream(selectTable).print("toChangelogStream>>>>>>>>");
        tableEnv.toRetractStream(selectTable, Row.class).print("toRetractStream>>>>>>>>");

        //TODO 6.启动任务
        env.execute();
    }
}
