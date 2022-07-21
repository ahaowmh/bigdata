package com.atguigu;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author ahao
 * @date 2022/6/18 09:40
 */
public class HbaseDML {
    // 静态属性 hbase连接
    public static Connection connection = null;

    static {
        // 0.创建配置对象并设置参数
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");
        // 1.创建Hbase连接
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2.关闭资源
    public static  void closeConnection() throws IOException {
        if(connection != null){
            connection.close();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(connection);
        HbaseDML.closeConnection();
    }
}
