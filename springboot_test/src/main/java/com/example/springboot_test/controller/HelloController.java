package com.example.springboot_test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.simpledbframework.DbExecutor;
import com.example.springboot_test.Dao.UserDao;
import com.example.springboot_test.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private DbExecutor dbExecutor;

    @Autowired
    private JobService jobService;


    //@SentinelResource(value = "buyProduct", blockHandler = "handleBlocked", fallback = "handelFallBack")
    @RequestMapping("/hello")
    public String hello(String flag) {

        if (jobService == null) {
            return "error";
        }

        return "success1111";

/*        if ("0".equals(flag)){
            throw new RuntimeException("模拟异常");
        }

        //userDao.getUserCount();
        Map param = new HashMap<String, String>();
        param.put("phone","15333617943");
        List result =  dbExecutor.select("Hello_Q001", param);
        System.out.println(result);
        return result.toString();*/
    }


    // 限流后处理逻 辑
    public String handleBlocked(String flag, BlockException ex) {
        return "服务器繁忙，请稍后再试";
    }

    // 熔断后处理逻辑
    public String handelFallBack(String flag, Throwable ex) {
        return "熔断：服务异常，请稍后再试";
    }

    public void execute() {
        log.info("helloController execute方法执行");
    }

}
