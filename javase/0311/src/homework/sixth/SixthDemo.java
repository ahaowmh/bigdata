package homework.sixth;

import org.junit.Test;

import java.io.*;

public class SixthDemo {
    @Test
    public void write () throws IOException {
        //创建流ObjectOutputStream
        OutputStream fos = new FileOutputStream("/Users/ahao/Documents/testIO/message.dat");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        //创建一个Message对象
        Message mes = new Message("ahao","普京","世界和平",88);
        //使用ObjectOutputStream写对象
        oos.writeObject(mes);
        //关闭资源
        oos.close();
        bos.close();
        fos.close();
    }
    @Test
    public void read () throws IOException, ClassNotFoundException {
        //创建ObjectInputStream
        InputStream fis = new FileInputStream("/Users/ahao/Documents/testIO/message.dat");
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        //使用ObjectInputStream读对象信息
        Object obj = ois.readObject();
        System.out.println(obj);

        //关闭ObjectInputStream
        ois.close();
        bis.close();
        fis.close();
    }
}
