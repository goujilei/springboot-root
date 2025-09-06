package com.example.springboot_test.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CronTask002 {

    private static final Logger log = LoggerFactory.getLogger(CronTask002.class);

    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        //log.info("任务执行时间：{}", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("==========开始生成理财产品基本信息文件==========");
        log.info("==========生成中==========");
        log.info("==========生成理财产品基本信息文件成功==========");
        CronTask002 cronTask002 = new CronTask002();
    }
}
