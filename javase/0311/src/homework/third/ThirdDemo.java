package homework.third;

import java.io.*;

public class ThirdDemo {
    public static void main(String[] args) throws IOException {
        //创建流
        Reader fr = new FileReader(new File("/Users/ahao/Documents/testIO/14章_IO流.md"));
        Writer fw = new FileWriter(new File("testIO/2.txt"));
        BufferedReader br = new BufferedReader(fr);
        BufferedWriter bw = new BufferedWriter(fw);
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
