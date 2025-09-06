package com.example.springboot_test.boot;

import com.example.springboot_test.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.swing.*;

@Component
public class JobStartupRunner implements ApplicationRunner {

    @Autowired
    private JobService jobService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jobService.registerAllEnabledJobs();
    }

/*    protected WebApplicationContext initWebApplicationContext() {
        // 传统模式下，这里就是 Root WebApplicationContext（ContextLoaderListener 放进去的）
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        WebApplicationContext wac = null;

        if (this.webApplicationContext != null) {      // A. 已经有现成的 WAC（直接用）
            wac = this.webApplicationContext;
        } else {
            wac = findWebApplicationContext();         // B. 去 ServletContext 里找指定属性名的 WAC
            if (wac == null) {
                // C. 都找不到 → 自己创建（传统 web.xml 常见：在这里创建“子容器”）
                wac = createWebApplicationContext(rootContext);
            }
        }



        onRefresh(wac); // 初始化 MVC 基础设施（找 HandlerMapping/Adapter/Controller 等）
        return wac;
    }*/
}