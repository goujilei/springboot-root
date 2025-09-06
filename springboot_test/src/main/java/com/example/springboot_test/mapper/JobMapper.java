package com.example.springboot_test.mapper;

import org.apache.ibatis.annotations.Param;
import com.example.springboot_test.entity.Job;


import java.util.List;

// mapper/JobMapper.java
public interface JobMapper {
    List<Job> selectAll();
    List<Job> selectByCondition(@Param("name") String name,
                                @Param("enabled") Boolean enabled,
                                @Param("invokeType") String invokeType);
    Job selectById(@Param("id") String id);
    int insert(Job job);
    int updateById(Job job);
    int deleteById(@Param("id") String id);

    List<Job> listAllEnabled();
}
