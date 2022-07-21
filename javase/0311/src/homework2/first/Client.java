package homework2.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //创建一个Socket，指定服务器的IP和监听端口
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 777);
        //键盘录入
        Scanner input = new Scanner(System.in);
        //发送消息
        PrintStream ps = new PrintStream(socket.getOutputStream());
        //接受消息
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("欢迎咨询尚硅谷！");
        while (true) {
            //发送一个请求
            String word = input.nextLine();
            ps.println(word);
            if("3q3q".equals(word)){
                break;
            }
            //接收来自服务器端的响应
            String result = br.readLine();
            System.out.println(result);
        }
        //关闭资源
        input.close();
        socket.close();
    }
}
