package homework.seventh;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class SeventhDemo {
    //从键盘输入一行行数据，然后再一行行的写入到另外的一个文件中
    @Test
    public void test01() throws IOException {
        Scanner input = new Scanner(System.in);
        PrintStream ps = new PrintStream("words.txt");
        for (int i = 0; i < 3; i++) {
            System.out.print("请输入一句话：");
            String str = input.nextLine();
            ps.println(str);//写完换行
        }
            input.close();
            ps.close();
    }
    @Test
    public void test03() throws IOException {
        //Scanner input = new Scanner(System.in);
        Scanner input = new Scanner(new FileInputStream("words.txt"));
        PrintStream ps = new PrintStream("words2.txt");

        while(input.hasNextLine()){
            String str = input.nextLine();
            ps.println(str);//写完换行
        }
        input.close();
        ps.close();
    }

}

