package homework;

import java.io.File;
import java.io.IOException;

public class First {
    public static void main(String[] args) throws IOException {
        //创建File对象,只是把字符串路径封装为File对象,不考虑路径的真假情况
        File f1 = new File("/Users/ahao/Documents/testIO/");
        //根据路径对象创建文件夹
        f1.mkdirs();
        //根据路径创建文件
        File f2  = new File("/Users/ahao/Documents/testIO/1.txt");
        f2.createNewFile();

        File f3 = new File("testIO");
        f3.mkdirs();
        File f4 = new File("testIO/1.txt");
        f4.createNewFile();

    }
}
