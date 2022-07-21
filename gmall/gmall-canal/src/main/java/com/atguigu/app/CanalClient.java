package com.atguigu.app;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.atguigu.constants.GmallConstants;
import com.atguigu.utils.MyKafkaSender;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author ahao
 * @date 2022/6/24 21:34
 */
public class CanalClient {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        //获取链接器
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("hadoop102", 11111), "example", "", "");

        while (true){
            //获取连接
            canalConnector.connect();

            //选择订阅的数据
            canalConnector.subscribe("gmall220212.*");

            //获取多个SQL执行的数据结果
            Message message = canalConnector.get(100);

            //获取单个SQL执行的结果
            List<CanalEntry.Entry> entries = message.getEntries();

            //判断list中是否有数据（MySQL中是否有变动）
            if (entries.size()<=0){
                //没有数据变化
                System.out.println("没有数据更新，休息一下。。。。");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                //有数据变化，获取每一个entry
                for (CanalEntry.Entry entry : entries) {
                    //获取表名
                    String tableName = entry.getHeader().getTableName();

                    //获取entry类型
                    CanalEntry.EntryType entryType = entry.getEntryType();

                    //根据entry类型判断是否需要反序列化
                    if (CanalEntry.EntryType.ROWDATA.equals(entryType)){
                        //获取序列化数据
                        ByteString storeValue = entry.getStoreValue();

                        //对数据做反序列化
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);

                        //获取事件类型
                        CanalEntry.EventType eventType = rowChange.getEventType();

                        //获取封装每一行数据的list集合
                        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();

                        //编写的方法，根据表名、事件类型判断要获取那张表里的数据
                        handle(tableName,eventType,rowDatasList);
                    }
                }
            }

        }
    }

    private static void handle(String tableName, CanalEntry.EventType eventType, List<CanalEntry.RowData> rowDatasList) {
        //通过表名和类型判断获取那张表中的哪些数据（新增数据）
        if ("order_info".equals(tableName)&& CanalEntry.EventType.INSERT.equals(eventType)){

            //获取每一行的数据
            for (CanalEntry.RowData rowData : rowDatasList) {
                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

                JSONObject jsonObject = new JSONObject();

                //获取每一行中每一列的额数据
                for (CanalEntry.Column column : afterColumnsList) {
                    jsonObject.put(column.getName(),column.getValue());
                }
                //打印JSON
                System.out.println(jsonObject.toJSONString());

                MyKafkaSender.send(GmallConstants.KAFKA_TOPIC_ORDER,jsonObject.toJSONString());

            }
        }
    }
    }

