package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestFileInputStream {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("/Users/ahao/Desktop/尚硅谷/IO.md");
        FileOutputStream fos = new FileOutputStream("/Users/ahao/Desktop/xxx/copy01.md");

        //读
        byte [] b = new byte[1024];
        int len;
        while((len= fis.read(b)) != -1) {
            //写
            fos.write(b,0,len);
        }
        //关闭资源
        fis.close();
        fos.close();

    }
}
