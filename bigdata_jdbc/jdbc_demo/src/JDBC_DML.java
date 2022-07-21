import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC_DML {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //获取连接
        //url 规则的是什么  协议+主机(ip)+端口
        //jdbc  主协议:子协议://主机(localhost):3306/库名
        String url = "jdbc:mysql://localhost:3306/mytest1";
        String user = "root";
        String password = "123123123";
        Connection connection = DriverManager.getConnection(url,user,password);

        //发送执行SQL
        Statement statement = connection.createStatement();
        //创建一个stu表
        //String sql  ="create table stu (id int primary key auto_increment,name varchar(25),age int)";
        //向表中添加元素
        //String sql = "insert into stu value (null,'艾伦',22)";
        //修改数据
        //String sql  ="update stu set age = 24 where id = 1";
        //删除表
        String sql = "delete from stu where id = 2";
        statement.executeUpdate(sql);
        //释放连接
        connection.close();
        statement.close();
    }
}
