package com.atguigu.kafka.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
/*
自定义分区器：
1. 实现接口Partitioner
2. 实现3个方法:partition,close,configure
3. 编写partition方法,返回分区号
*/
public class CustomPartition implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //自定义功能：含有atguigu的消息，发送到0号分区，其他到1号分区
        if (o1.toString().contains("atguigu")) {
            return 0;
        }
        return 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
