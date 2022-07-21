package com.atguigu.day09;

import com.atguigu.bean.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author ahao
 * @date 2022/7/11 19:03
 */
public class CET_LoginWithState {

    public static void main(String[] args) throws Exception {

        //TODO 1.创建流的执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        
        //TODO 2.从文件获取数据
        DataStreamSource<String> streamSource = env.readTextFile("input/LoginLog.csv");

        //TODO 3.将数据转换成JavaBean
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

        //TODO 5.使用状态编程的方式获取连续登录失败两次的记录
        SingleOutputStreamOperator<String> outputStreamOperator = keyedStream.flatMap(new RichFlatMapFunction<LoginEvent, String>() {

            private ValueState<LoginEvent> valueState;

            @Override
            public void open(Configuration parameters) throws Exception {
                valueState = getRuntimeContext().getState(new ValueStateDescriptor<LoginEvent>("value-state", LoginEvent.class));
            }

            @Override
            public void flatMap(LoginEvent value, Collector<String> out) throws Exception {
                if ("fail".equals(value.getEventType())) {

                    //取出状态中的数据
                    LoginEvent lastValue = valueState.value();

                    //判断状态中是否为空，两次时间相差是否在两秒内
                    if (lastValue != null && value.getEventTime() - lastValue.getEventTime() <= 2) {
                        out.collect(lastValue.getUserId() + "在" + lastValue.getEventTime() + "和" + value.getEventTime() + "登录失败两次");

                    }
                    valueState.update(value);

                } else {
                    valueState.clear();
                }
            }
        });


        //TODO 8.打印
        outputStreamOperator.print("=========-");

        //TODO 9.执行
        env.execute();
    }
}
