package com.example.springboot_test.entity;

import com.example.springboot_test.enums.HttpMethod;
import com.example.springboot_test.enums.InvokeType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// entity/JobEntity.java
@Data
@Getter
@Setter
public class Job {
    private String id;

    private String name;
    private String cron;
    private Boolean enabled;
    private String remark;
    private Integer timeoutSec;
    private Integer retry;

    private InvokeType invokeType;

    private String beanName;
    private String className;
    private String methodName;

    private String httpUrl;

    private HttpMethod httpMethod;


    private String argsJson; // 存 JSON 字符串

    private Long createdAt;
    private Long updatedAt;
}
