package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

//带回调函数的异步发送
public class CustomProducerWithCallBack {
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
            producer.send(new ProducerRecord<String, String>("first", "hi" + i),
                    //添加回调函数
                    //通常使用匿名内部类方式
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            if (e != null) {
                                //发送失败
                                e.printStackTrace();
                            }else {
                                //发送成功
                                System.out.println("主题是：" + recordMetadata.topic() + "分区是：" + recordMetadata.partition());
                            }
                        }
                    }
            );
        }
        //5、关闭资源
        producer.close();
    }

}
