package com.lht.bio;


import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池机制
 * 思路：
 * ①创建一个线程池
 * ②如果有客户端连接，就创建一个线程，与之通信（单独写一个方法）
 * @author lhtao
 * @date 2020/9/29 14:15
 */
public class BioServer {

    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while(true) {
            System.out.println("等待连接...");
            //监听,等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程，与之通信
            threadPool.execute(()->handler(socket));
        }
    }



    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                System.out.println("等待read...");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.println("当前线程id: " + Thread.currentThread().getId() + ", 当前线程名称: " + Thread.currentThread().getName());
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
