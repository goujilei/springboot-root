package com.example.simpledbframework.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "db")
@Getter
@Setter
public class DbConfig {
    private String url;
    private String username;
    private String password;

    // getter/setter...
}
