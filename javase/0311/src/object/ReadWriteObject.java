package object;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ReadWriteObject {

    public void save() throws IOException {
        Student.setSchool("尚硅谷");
        Student s = new Student("王明好","松江",24);

        //创建序列化流对象
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.dat"));
        //写对象
        oos.writeObject(s);
        //释放资源
        oos.close();
        System.out.println("姓名地址被序列化，年龄没有");
    }

    public void reload () {
        //创建反序列化流

    }
}
