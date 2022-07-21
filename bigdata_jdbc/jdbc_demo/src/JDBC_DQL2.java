import java.sql.*;

public class JDBC_DQL2 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest1?useSSL=false","root","123123123");

        //发送SQL语句
        String sql = "select * from emp";
        Statement statement =connection.createStatement();
        //resultset是返回集
        //查询语句要用executequery
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()){
            int id = rs.getInt(1);
            String last_name = rs.getString(2);
            int age = rs.getInt(3);

            System.out.println(id + "\t" + last_name + "\t" + age);
        }
        //释放连接
        rs.close();
        statement.close();
        connection.close();
    }
}
