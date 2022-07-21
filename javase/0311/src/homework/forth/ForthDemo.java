package homework.forth;

import java.io.*;

public class ForthDemo {
    public static void main(String[] args) throws IOException {
        //创建流
        FileInputStream fr = new FileInputStream("/Users/ahao/Desktop/尚硅谷/作业/我想对你说.txt");
        FileOutputStream fw = new FileOutputStream("testIO/柴老师的话.txt");
        InputStreamReader isr = new InputStreamReader(fr,"GBK");
        OutputStreamWriter osw = new OutputStreamWriter(fw,"UTF-8");
        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osw);
        //逐行读
        String str = br.readLine();
        while (str != null) {
            //逐行写
            bw.write(str);
            bw.newLine();
            str = br.readLine();
        }
        //关闭流
        br.close();
        bw.close();
        fr.close();
        fw.close();
    }
}
