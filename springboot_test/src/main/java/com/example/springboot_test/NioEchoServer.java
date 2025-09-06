package com.example.springboot_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioEchoServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. 创建 ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        System.out.println("总channel:"+serverChannel);
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false); // 非阻塞模式

        // 2. 创建 Selector，并将 serverChannel 注册进去
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动，监听端口 8080");
        Thread.sleep(5000);

        // 3. 事件循环
        while (true) {
            selector.select(); // 阻塞直到有事件发生
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove(); // 移除已处理事件

                if (key.isAcceptable()) {
                    // 处理连接事件
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    System.out.println("事件channel:"+serverChannel);
                    System.out.println(serverChannel == server? "一样":"不一样");
                    SocketChannel clientChannel = server.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("新连接：" + clientChannel.getRemoteAddress());

                } else if (key.isReadable()) {
                    // 处理读事件
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = client.read(buffer);

                    if (read == -1) {
                        System.out.println("客户端断开：" + client.getRemoteAddress());
                        client.close();
                    } else {
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        String message = new String(data);
                        System.out.println("收到：" + message.trim());

                        // Echo 回去
                        client.write(ByteBuffer.wrap(("你说：" + message).getBytes()));
                    }
                }
            }
        }
    }
}
