package com.atguigu.day04;

import com.atguigu.bean.AdsClickLog;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ahao
 * @date 2022/7/5 08:54
 */
public class Flink_Project_Ads_Click {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        env
                .readTextFile("input/AdClickLog.csv")
                .map(new MapFunction<String, Tuple2<String,Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        String[] split = value.split(",");
                        AdsClickLog adsClickLog = new AdsClickLog(
                                Long.parseLong(split[0]),
                                Long.parseLong(split[1]),
                                split[2],
                                split[3],
                                Long.parseLong(split[4])
                        );
                        return Tuple2.of(adsClickLog.getProvince()+"-"+adsClickLog.getAdId(),1);
                    }
                })
                .keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }

}
