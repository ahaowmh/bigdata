package homework.second;

import java.io.File;
import java.io.IOException;

public class SecondDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/ahao/Documents/testIO/a.txt");
        if (!file.exists()) {   //判断指定地址是否存在指定文件
            //不存在则创建
            file.createNewFile();
        }
        //打印文件名
        System.out.println(file.getName());
        //打印文件大小
        System.out.println(file.length());
        //文件的绝对路径
        System.out.println(file.getAbsoluteFile());
        //文件的父路径
        System.out.println(file.getParentFile());

        File file1 = new File("/Users/ahao/Documents/testIO");
        //判断是否为文件/文件夹
        if (file1.isFile()) {
            System.out.println(file1.getName() + "是一个文件");
        } else if (file1.isDirectory()){
            System.out.println(file1.getName() +"是一个文件夹");
        }

        //删除testIO文件下的a.txt文件
        file.delete();
        //删除当前模块下的testIO文件下的1.txt文件
        File f1 = new File("testIO/1.txt");
        f1.delete();
        //删除testIO文件夹
        f1.getParentFile().delete();

    }
}

