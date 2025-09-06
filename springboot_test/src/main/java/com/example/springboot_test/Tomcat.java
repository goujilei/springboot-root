package com.example.springboot_test;

import com.example.springboot_test.entity.User;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.*;

public class Tomcat {
    public static void main(String[] args) throws IOException {
/*        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("MiniTomcat 启动，监听端口 8080");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new RequestHandler(socket)).start();
        }*/

        User user1 = new User();
        Optional<User> user = Optional.ofNullable(user1);
        System.out.println(user.get());
        user1.setUserPwd("123456");
        user.orElse(user1);
        System.out.println(user.orElse(user1));
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<?> submit = pool.submit(() -> System.out.println(user.orElse(user1)));
        Future<?> submit1 = pool.submit(() -> System.out.println(user.orElse(user1)));
        Future<?> submit2 = pool.submit(() -> {
            System.out.println(123);
            return "123";
        });
        Thread thread = new Thread(new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }));

        thread.start();
    }
}
