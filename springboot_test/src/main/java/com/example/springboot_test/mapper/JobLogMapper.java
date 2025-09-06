package com.example.springboot_test.mapper;

import com.example.springboot_test.entity.JobLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// mapper/JobLogMapper.java
public interface JobLogMapper {
    int insert(JobLog log);
    List<JobLog> selectByCondition(@Param("jobName") String jobName,
                                   @Param("status") String status);
}