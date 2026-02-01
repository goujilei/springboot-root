package com.example.springboot_test.cron;

import com.example.springboot_test.controller.HelloController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CronTask001 {

/*    @Autowired
    private DbExecutor dbExecutor;*/

    private static final Logger log = LoggerFactory.getLogger(CronTask001.class);

    // 每5秒执行一次
    //@Scheduled(fixedRate = 5000)
    //@Scheduled(cron = "*/5  * * * * ?")
    public void runEveryFiveSeconds() {
        log.info("==========开始执行生成生成批量资金文件任务==========");

        LocalDateTime now = LocalDateTime.now();
        log.info("任务执行时间：{}", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("==========执行生成生成批量资金文件任务成功==========");
    }

    // 每天凌晨1点执行
    // @Scheduled(cron = "0 0 1 * * ?")
    public void runAtOneAM() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM1() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM2() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM3() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM4() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM5() {
        System.out.println("凌晨1点执行任务");
    }

    public void runAtOneAM6() {
        System.out.println("凌晨1点执行任务");
    }
}
