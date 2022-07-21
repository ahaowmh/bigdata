package tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws IOException {
        //创建一个Socket,指定服务器IP和监听端口
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),8888);

        //发送请求
        OutputStream os = socket.getOutputStream();
        os.write("冰冻三尺非一日之寒".getBytes());

        //关闭资源
        os.close();
        socket.close();
    }
}
