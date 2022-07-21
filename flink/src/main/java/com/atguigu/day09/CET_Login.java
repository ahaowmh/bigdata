package com.atguigu.day09;

import com.atguigu.bean.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/7/11 19:03
 */
public class CET_Login {

    public static void main(String[] args) throws Exception {

        //TODO 1.创建流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        
        //TODO 2.从文件获取数据
        DataStreamSource<String> streamSource = env.readTextFile("input/LoginLog.csv");

        //TODO 3.将数据转换成JavaBean
       /* SingleOutputStreamOperator<LoginEvent> map = streamSource.map((MapFunction<String, LoginEvent>) value -> {
            String[] split = value.split(",");
            return new LoginEvent(Long.parseLong(split[0]), split[1], split[2], Long.parseLong(split[3]));
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<LoginEvent>forBoundedOutOfOrderness(Duration.ofSeconds(1)).withTimestampAssigner(new SerializableTimestampAssigner<LoginEvent>() {
            @Override
            public long extractTimestamp(LoginEvent element, long recordTimestamp) {
                return element.getEventTime() * 1000L;
            }
        }));*/

        SingleOutputStreamOperator<LoginEvent> loginEventDS = streamSource.map(line -> {
            String[] split = line.split(",");
            return new LoginEvent(
                    Long.parseLong(split[0]),
                    split[1],
                    split[2],
                    Long.parseLong(split[3])
            );
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<LoginEvent>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                .withTimestampAssigner(new SerializableTimestampAssigner<LoginEvent>() {
                    @Override
                    public long extractTimestamp(LoginEvent element, long recordTimestamp) {
                        return element.getEventTime() * 1000L;
                    }
                }));

        //TODO 4.按照use_id分组
        KeyedStream<LoginEvent, Long> keyedStream = loginEventDS.keyBy(LoginEvent::getUserId);

        //TODO 5.定义模式序列
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("start").where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return "fail".equals(value.getEventType());
                    }
                }).times(2)
                .consecutive()
                .within(Time.seconds(2));


        //TODO 6.将模式序列作用到流上
        PatternStream<LoginEvent> patternStream = CEP.pattern(keyedStream, pattern);

        //TODO 7.提取事件
        SingleOutputStreamOperator<String> selectDS = patternStream.select(new PatternSelectFunction<LoginEvent, String>() {
            @Override
            public String select(Map<String, List<LoginEvent>> map) throws Exception {
                List<LoginEvent> events = map.get("start");
                LoginEvent start = events.get(0);
                LoginEvent end = events.get(1);
                return start.getUserId() + "在" + start.getEventTime() + "和" + end.getEventTime() + "登录失败两次";
            }
        });

        //TODO 8.打印
        selectDS.print("=============");

        //TODO 9.执行
        env.execute();
    }
}
