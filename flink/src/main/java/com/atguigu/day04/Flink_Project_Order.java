package com.atguigu.day04;

import com.atguigu.bean.OrderEvent;
import com.atguigu.bean.TxEvent;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.HashMap;

/**
 * @author ahao
 * @date 2022/7/5 10:14
 */
public class Flink_Project_Order {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        //读取数据---》转换成JavaBean
        SingleOutputStreamOperator<OrderEvent> orderEventDS  = env.readTextFile("input/OrderLog.csv")
                .map(line -> {
                    String[] split = line.split(",");
                    return new OrderEvent(
                            Long.parseLong(split[0]),
                            split[1],
                            split[2],
                            Long.parseLong(split[3])
                    );
                });
        SingleOutputStreamOperator<TxEvent> txDS = env
                .readTextFile("input/ReceiptLog.csv")
                .map(line -> {
                    String[] datas = line.split(",");
                    return new TxEvent(datas[0], datas[1], Long.valueOf(datas[2]));
                });
        //将两条流合并
        ConnectedStreams<OrderEvent, TxEvent> connect = orderEventDS.connect(txDS);

        //将相同交易码的数据合并
        ConnectedStreams<OrderEvent, TxEvent> keyBy = connect.keyBy("txId", "txId");

        //对账
        keyBy.process(new KeyedCoProcessFunction<String, OrderEvent, TxEvent, String>() {

            //存放缓存数据
            HashMap<String, OrderEvent> orderEventHashMap = new HashMap<>();
            HashMap<String, TxEvent> txEventHashMap = new HashMap<>();

            @Override
            public void processElement1(OrderEvent value, KeyedCoProcessFunction<String, OrderEvent, TxEvent, String>.Context ctx, Collector<String> out) throws Exception {
                //去对方缓存区对账
                if (txEventHashMap.containsKey(value.getTxId())){
                    //对账成功
                    System.out.println("订单"+value.getOrderId()+"对账成功");
                    //将对账成功的数据从缓存区中移除,防止hashmap内存不足
                    txEventHashMap.remove(value.getTxId());
                }else {
                    //对账失败，将此订单信息放到对方缓存区
                    orderEventHashMap.put(value.getTxId(),value);
                }
            }

            @Override
            public void processElement2(TxEvent value, KeyedCoProcessFunction<String, OrderEvent, TxEvent, String>.Context ctx, Collector<String> out) throws Exception {
                //  去对方缓存区查账
                if (orderEventHashMap.containsKey(value.getTxId())){
                    //对账成功
                    System.out.println("订单"+orderEventHashMap.get(value.getTxId()).getOrderId()+"对账成功");
                    //将对账成功的数据从缓存区中移除,防止hashmap内存不足
                    orderEventHashMap.remove(value.getTxId());
                }else {
                    //失败
                    txEventHashMap.put(value.getTxId(),value);
                }
            }
        }).print();

        env.execute();
    }
}
