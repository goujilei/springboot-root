package com.example.springboot_test.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// entity/JobLogEntity.java
@Data
@Getter
@Setter
public class JobLog {
    private String id;
    private String jobId;
    private String jobName;
    private Long startTime;
    private Long endTime;
    private String status; // SUCCESS / FAIL
    private String message;
    private String resultJson;
}
