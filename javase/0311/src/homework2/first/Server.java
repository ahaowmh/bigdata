package homework2.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //指定监听的端口
        ServerSocket serverSocket = new ServerSocket(777);
        //调用 accept()监听连接请求
        Socket socket = serverSocket.accept();
        //接收消息
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //发送消息
        PrintStream ps = new PrintStream(socket.getOutputStream());
        while (true) {
            //输入客户端请求信息
            String info = br.readLine();
            if ("3q3q".equals(info)) {
                break;
            }
            //显示客户端留言信息
            System.out.println(socket.getInetAddress() + "留言" + info);
            //给出客户端响应
            if ("你好，我想报名这个月的JavaEE就业班！".equals(info)) {
                ps.println("这个月的所有期班都已经满了，只能报下一个月的了！");
            } else if ("好的，赶紧给我占个座！".equals(info)) {
                ps.println("好的！");
            } else {
                ps.println("我不知道你在说什么。。。");
            }
        }
        //关闭资源
        socket.close();
        serverSocket.close();
    }
}
