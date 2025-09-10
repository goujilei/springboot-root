package com.example.springboot_test.service;

import com.example.springboot_test.entity.Job;
import com.example.springboot_test.mapper.JobMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /** 启动时调用：把库里“启用”的任务全部注册到 Quartz */
    public void registerAllEnabledJobs() throws Exception {
        List<Job> list = jobMapper.listAllEnabled(); // 你在 Mapper 里实现：WHERE enabled=1
        for (Job j : list) {
            schedule(j);        }
    }

    public List<Job> list(String name, Boolean enabled, String invokeType) {
        return jobMapper.selectByCondition(name, enabled, invokeType);
    }

    public Job create(Job j) throws Exception {
        long now = System.currentTimeMillis();
        j.setId(UUID.randomUUID().toString());
        j.setCreatedAt(now);
        j.setUpdatedAt(now);
        if (j.getEnabled() == null) j.setEnabled(Boolean.TRUE);
        jobMapper.insert(j);
        if (Boolean.TRUE.equals(j.getEnabled())) {
            schedule(j);
        }
        return j;
    }

    public Job update(String id, Job patch) throws Exception {
        Job db = jobMapper.selectById(id);
        if (db == null) throw new IllegalStateException("job not found: " + id);

        boolean reschedule =
                !equalsStr(db.getCron(), patch.getCron()) ||
                        !equalsBool(db.getEnabled(), patch.getEnabled()) ||
                        !equalsStr(db.getInvokeType().toString(), patch.getInvokeType().toString()) ||
                        !equalsStr(db.getBeanName(), patch.getBeanName()) ||
                        !equalsStr(db.getClassName(), patch.getClassName()) ||
                        !equalsStr(db.getMethodName(), patch.getMethodName()) ||
                        !equalsStr(db.getHttpUrl(), patch.getHttpUrl());

        // merge（按需覆盖）
        if (patch.getName() != null) db.setName(patch.getName());
        if (patch.getCron() != null) db.setCron(patch.getCron());
        if (patch.getEnabled() != null) db.setEnabled(patch.getEnabled());
        if (patch.getRemark() != null) db.setRemark(patch.getRemark());
        if (patch.getTimeoutSec() != null) db.setTimeoutSec(patch.getTimeoutSec());
        if (patch.getRetry() != null) db.setRetry(patch.getRetry());
        if (patch.getInvokeType() != null) db.setInvokeType(patch.getInvokeType());
        if (patch.getBeanName() != null) db.setBeanName(patch.getBeanName());
        if (patch.getClassName() != null) db.setClassName(patch.getClassName());
        if (patch.getMethodName() != null) db.setMethodName(patch.getMethodName());
        if (patch.getHttpUrl() != null) db.setHttpUrl(patch.getHttpUrl());
        if (patch.getHttpMethod() != null) db.setHttpMethod(patch.getHttpMethod());
        if (patch.getArgsJson() != null) db.setArgsJson(patch.getArgsJson());
        db.setUpdatedAt(System.currentTimeMillis());

        jobMapper.updateById(db);

        if (reschedule) {
            unschedule(db.getId());
            if (Boolean.TRUE.equals(db.getEnabled())) schedule(db);
        }
        return db;
    }

    public void delete(String id) throws Exception {
        unschedule(id);
        jobMapper.deleteById(id);
    }

    public Job toggle(String id) throws Exception {
        Job db = jobMapper.selectById(id);
        if (db == null) throw new IllegalStateException("job not found: " + id);
        db.setEnabled(!Boolean.TRUE.equals(db.getEnabled()));
        db.setUpdatedAt(System.currentTimeMillis());
        jobMapper.updateById(db);
        if (db.getEnabled()) schedule(db); else unschedule(id);
        return db;
    }

    public void runNow(String id) throws Exception {
        JobKey key = jobKey(id);
        if (!scheduler.checkExists(key)) {
            Job db = jobMapper.selectById(id);
            if (db != null) schedule(db);
        }
        scheduler.triggerJob(jobKey(id));
    }

    /* ===== Quartz 注册/注销 ===== */
    private void schedule(Job j) throws Exception {
        JobDetail detail = JobBuilder.newJob(com.example.springboot_test.service.DispatcherJob.class)
                .withIdentity(jobKey(j.getId()))
                .usingJobData("jobId", j.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey(j.getId()))
                .withSchedule(CronScheduleBuilder.cronSchedule(j.getCron()))
                .build();

        scheduler.scheduleJob(detail, trigger);
        scheduler.start();
    }

    private void unschedule(String id) throws Exception {
        scheduler.deleteJob(jobKey(id));
    }

    private JobKey jobKey(String id) { return new JobKey("job:" + id, "default"); }
    private TriggerKey triggerKey(String id) { return new TriggerKey("tr:" + id, "default"); }

    private boolean equalsStr(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
    private boolean equalsBool(Boolean a, Boolean b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.booleanValue() == b.booleanValue();
    }
}
