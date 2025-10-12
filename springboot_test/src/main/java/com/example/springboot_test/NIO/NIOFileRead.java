package com.example.springboot_test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class NIOFileRead {
    public static void main(String[] args) throws Exception {
        File file = new File("data.txt");
        if (!file.exists()) {
            Files.write(file.toPath(), "默认内容：Hello NIO!".getBytes());
            System.out.println("自动创建 data.txt 并写入内容");
        } else {
            System.out.println("文件已存在");
        }
        // 1. 创建 FileChannel
        FileInputStream fis = new FileInputStream("data.txt");
        FileChannel channel = fis.getChannel();

        // 2. 创建 Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3. 读取数据到 Buffer
        int bytesRead = channel.read(buffer);
        while (bytesRead != -1) {
            buffer.flip(); // 读模式
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear(); // 切回写模式
            bytesRead = channel.read(buffer);
        }

        channel.close();
        fis.close();
    }
}
