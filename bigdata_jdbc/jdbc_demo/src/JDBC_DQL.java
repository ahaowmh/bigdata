import java.sql.*;

public class JDBC_DQL {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //注册驱动
        Class.forName("com.mysql.jdbc.Driver");

        //连接数据库
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest1","root","123123123");

        //执行sql
        String sql = "SELECT * FROM myemp11";
        Statement st =conn.createStatement();

        ResultSet rs = st.executeQuery(sql);//resultset看成inputstream

        while (rs.next()){
            Object did = rs.getObject(1);
            Object dname = rs.getObject(2);
            Object desc = rs.getObject(3);

            System.out.println(did + "\t" + dname +"\t" + desc);
        }
        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }
}
