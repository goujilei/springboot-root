package com.example.springboot_test.controller;

import com.example.springboot_test.entity.Job;
import com.example.springboot_test.mapper.JobMapper;
import com.example.springboot_test.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired private JobService jobService;
    @Autowired private JobMapper jobMapper;

    @GetMapping
    public List<Job> list(@RequestParam(required=false) String name,
                          @RequestParam(required=false) Boolean enabled,
                          @RequestParam(required=false) String invokeType) {
        return jobService.list(name, enabled, invokeType);
    }

    @PostMapping
    public Job create(@RequestBody Job j) throws Exception {
        return jobService.create(j);
    }

    @PatchMapping("/{id}")
    public Job update(@PathVariable String id, @RequestBody Job j) throws Exception {
        j.setId(id);
        return jobService.update(id, j);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws Exception {
        jobService.delete(id);
    }

    @PatchMapping("/{id}/toggle")
    public Job toggle(@PathVariable String id) throws Exception {
        return jobService.toggle(id);
    }

    @PostMapping("/{id}/run")
    public void run(@PathVariable String id) throws Exception {
        jobService.runNow(id);
    }
}