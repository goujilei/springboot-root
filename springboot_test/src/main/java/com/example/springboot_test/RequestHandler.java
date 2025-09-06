package com.example.springboot_test;

import java.io.*;
import java.net.Socket;

class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            // 读取请求头第一行：GET /hello HTTP/1.1
            String line = reader.readLine();
            if (line == null || line.isEmpty()) return;

            String[] parts = line.split(" ");
            String method = parts[0];
            String path = parts[1];

            // 简单的路由判断
            String body;
            if ("/hello".equals(path)) {
                body = "<h1>Hello, world!</h1>";
            } else {
                body = "<h1>404 Not Found</h1>";
            }

            // 返回 HTTP 响应
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: text/html; charset=utf-8\r\n");
            writer.write("Content-Length: " + body.getBytes().length + "\r\n");
            writer.write("\r\n"); // 头部结束
            writer.write(body);   // 响应体
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
