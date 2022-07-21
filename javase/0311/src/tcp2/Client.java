package tcp2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {

        //创建一个Socket,指定服务器IP和监听端口
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"),8888);

        //发送请求
        OutputStream os = socket.getOutputStream();
        os.write("冰冻三尺非一日之寒".getBytes());

        //接受服务端的相应
        InputStream is = socket.getInputStream();
        byte [] bytes = new byte[1024];
        int len = is.read(bytes);
        String result = new String(bytes, 0, len);
        System.out.println("服务端发来的成语反转是："+result);


        //关闭资源
        os.close();
        socket.close();
    }
}
