package com.example.simpledbframework;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.simpledbframework")
public class DbAutoConfiguration {

    @Autowired
    private SqlBootstrap sqlBootstrap;

    @PostConstruct
    public void init() {
        sqlBootstrap.init(); // 自动初始化
    }
}