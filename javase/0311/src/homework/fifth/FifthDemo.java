package homework.fifth;

import org.junit.Test;

import java.io.*;

public class FifthDemo {
    @Test
    public  void test01() throws IOException {
        //创建DataOutputStream     提速/简化
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("/Users/ahao/Documents/testIO/data.dat")));
        //使用DataOutputStream写
        dos.writeInt(10);
        dos.writeChar('a');
        dos.writeDouble(2.5);
        dos.writeBoolean(true);
        dos.writeUTF("尚硅谷");

        //关闭DataOutputStream
        dos.close();
    }
    @Test
    public void test02() throws IOException {
        //创建DataInputStream
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream("/Users/ahao/Documents/testIO/data.dat")));
        //使用DataInputStream
        System.out.println(dis.readInt());
        System.out.println(dis.readChar());
        System.out.println(dis.readDouble());
        System.out.println(dis.readBoolean());
       // System.out.println(dis.readUTF());

        //关闭DataInputStream
        dis.close();
    }

}






