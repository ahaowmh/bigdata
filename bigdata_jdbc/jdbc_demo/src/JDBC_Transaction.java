import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_Transaction {
    public static void main(String[] args) throws SQLException {
        //注册驱动
        //Class.forName("com.mysql.jdbc.Driver");
        DriverManager.registerDriver(new Driver());
        //获取连接
        Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest1?useSSL=false","root","123123123");
        //关闭自动提交
        connection.setAutoCommit(false);
        //创建对象，多次使用
        PreparedStatement pst = connection.prepareStatement("");
        //准备SQL语句
        String sql1 = "insert into  emp value(null ,'石原里美',20) ";
        //开始执行
        try{
            pst.executeUpdate(sql1);
            System.out.println("执行成功");
            //提交事务
            connection.commit();
        }catch (Exception e){
            System.out.println("执行失败");
            //若有异常则回滚
            connection.rollback();
        }finally{
            //释放资源
            pst.close();
            //恢复自动提交
            connection.setAutoCommit(true);
            connection.close();
        }

    }
}
