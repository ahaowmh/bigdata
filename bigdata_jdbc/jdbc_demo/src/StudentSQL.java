import com.mysql.jdbc.Connection;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentSQL {
    public static void main(String[] args) throws SQLException {
        Student baiwan = new Student("baiwan",18);
        Student happy = new Student("happy",3);
        addStudent(baiwan);
    }
    public static void addStudent(Student s) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;
        try{
            //注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            //获取连接
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest1?useSSL=false","root","123123123");
            //准备预编译对象
            String sql = "insert into stu value(null,?,?)";
            pst = connection.prepareStatement(sql);
            pst.setString(1,s.getName());
            pst.setInt(2,s.getAge());
            //执行SQL
            pst.executeUpdate();
            System.out.println("添加成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("添加失败");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            pst.close();
            connection.close();
        }
    }
}
