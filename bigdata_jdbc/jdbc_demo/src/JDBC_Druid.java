import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.SQLException;

public class JDBC_Druid {
    public static void main(String[] args) {
        //创建连接池对象
        DruidDataSource ds = new DruidDataSource();
        //通过set***方法给连接对象赋值（驱动器、数据库表地址、用户、密码）
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/mytest1");
        ds.setUsername("root");
        ds.setPassword("123123123");
        //连接池对象上限
        ds.setMaxActive(20);

        //设置一个最大等待时长，当用户申请连接数超过连接池中对象数量时，
        // 系统发出'网络不通畅'提示。
        ds.setMaxWait(5000);//5秒

        try{
            for (int i = 0; i < 100; i++) {
                DruidPooledConnection connection = ds.getConnection();
                System.out.println("第" + i + connection);
                //用完再放回连接池,每次获取的连接对象相同。若不放回，达到上限就不能获取了
                //connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("当前网络不通畅，开通VIP后极速浏览");
        }
    }
}
