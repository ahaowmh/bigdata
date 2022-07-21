package tcp2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //创建一个ServerSocket来指定监听的端口
        ServerSocket serverSocket = new ServerSocket(8888);

        //使用serverSocket进行监听
        Socket socket = serverSocket.accept();//一直监听

        //输出Client的请求信息
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = is.read(bytes);
        String info = new String(bytes, 0, len);
        System.out.println("客户端发出的请求信息是：" + info);

        //给客户端相应
        String react = new StringBuilder(info).reverse().toString();
        OutputStream os = socket.getOutputStream();
        os.write(react.getBytes());

        //关闭资源
        socket.close();
        serverSocket.close();
    }
}
