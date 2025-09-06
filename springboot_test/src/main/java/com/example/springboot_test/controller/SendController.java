package com.example.springboot_test.controller;

import com.example.springboot_test.configuration.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/send")
    public String send(String massager) {
        amqpTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, massager);
        log.info("消息{}已发送", massager);
        return "消息已发送";
    }
}
