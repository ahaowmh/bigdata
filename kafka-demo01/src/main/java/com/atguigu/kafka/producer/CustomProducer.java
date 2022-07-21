package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CustomProducer {
    public static void main(String[] args) {
        //1、创建配置对象
        Properties properties = new Properties();
        //2、添加配置参数
        //必要参数
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //3、创建kafka生产者对象
        //k v 结构
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        //4、调用send方法发送消息
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<String,String>("first","hello" + i));
        }
        //5、关闭资源
        producer.close();
    }
}
