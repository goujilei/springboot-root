package com.example.springboot_test.controller;

import com.example.springboot_test.configuration.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

@Component
public class MessageReceiver {

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleMessage(String msg) {
        System.out.println("接收到消息：" + msg);
    }

    public void a(String msg) {
        System.out.println("接收到消息：" + msg);
    }



}
