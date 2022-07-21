import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_DML2 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //获取连接
        //url 规则的是什么  协议+主机(ip)+端口
        //jdbc  主协议:子协议://主机(localhost):3306/库名
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest1?useSSL=false","root","123123123");

        //发送执行SQL语句
        Statement statement = connection.createStatement();
        //创建表
        //String sql = "create table emp(id int primary key auto_increment,last_name varchar(25),age int)";
        //添加元素
        //String sql = "insert into  emp value(null ,'木村拓哉',28) ";
        //修改元素
        //String sql = "update emp set age = 32 where id = 2";
        //删除记录
        String sql = "delete from emp where id = 3";
        statement.executeUpdate(sql);

        //释放连接
        statement.close();
        connection.close();

    }
}
