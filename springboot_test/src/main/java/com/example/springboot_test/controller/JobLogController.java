package com.example.springboot_test.controller;
import com.example.springboot_test.entity.JobLog;
import com.example.springboot_test.mapper.JobLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-logs")
public class JobLogController {

    @Autowired private JobLogMapper logMapper;

    @GetMapping
    public List<JobLog> list(@RequestParam(required=false) String jobName,
                             @RequestParam(required=false) String status) {
        return logMapper.selectByCondition(jobName, status);
    }
}