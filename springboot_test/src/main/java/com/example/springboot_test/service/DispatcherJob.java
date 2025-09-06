package com.example.springboot_test.service;

import com.example.springboot_test.entity.Job;
import com.example.springboot_test.entity.JobLog;
import com.example.springboot_test.mapper.JobLogMapper;
import com.example.springboot_test.mapper.JobMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class DispatcherJob implements org.quartz.Job {

    @Autowired private JobMapper jobMapper;
    @Autowired private JobLogMapper logMapper;
    @Autowired private ApplicationContext ctx;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobId = context.getMergedJobDataMap().getString("jobId");
        Job j = jobMapper.selectById(jobId);

        long start = System.currentTimeMillis();
        JobLog log = new JobLog();
        log.setId(UUID.randomUUID().toString());
        log.setJobId(jobId);
        log.setJobName(j != null ? j.getName() : "unknown");
        log.setStartTime(start);
        try {
            if (j == null) throw new IllegalStateException("Job not found: " + jobId);

            Object[] args;
            if (j.getArgsJson() == null || j.getArgsJson().trim().isEmpty() || "null".equals(j.getArgsJson().trim())) {
                args = null;
            } else {
                args = om.readValue(j.getArgsJson(), Object[].class);
            }

            String type = j.getInvokeType().toString();
            if ("SPRING_BEAN".equals(type)) {
                Object bean = ctx.getBean(j.getBeanName());
                Method method = findMethod(bean.getClass(), j.getMethodName(), args);
                Object res = method.invoke(bean, args);
                log.setStatus("SUCCESS");
                log.setMessage("SPRING_BEAN ok");
                log.setResultJson(om.writeValueAsString(res));
            } else if ("CLASS_REF".equals(type)) {
                Class<?> clazz = Class.forName(j.getClassName());

                Object target;
                try {
                    target = ctx.getBean(clazz);
                } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
                    String[] names = ctx.getBeanNamesForType(clazz);
                    if (names != null && names.length > 0) {
                        target = ctx.getBean(names[0]);
                    } else {
                        target = ctx.getAutowireCapableBeanFactory().createBean(clazz);
                    }
                }

                Method method = findMethod(clazz, j.getMethodName(), args);
                Object res = method.invoke(target, args);

                log.setStatus("SUCCESS");
                log.setMessage("CLASS_REF ok");
                log.setResultJson(om.writeValueAsString(res));
            } else if ("HTTP".equals(type)) {
                ResponseEntity<String> resp;
                String method = j.getHttpMethod() == null ? "GET" : j.getHttpMethod().toString().toUpperCase();

                // 将参数数组转为 Map<String, Object>（仅限 2 元数组形式：["key", "val"]）
                Map<String, Object> paramMap = convertToParamMap(args);

                if ("GET".equals(method)) {
                    UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(j.getHttpUrl());
                    for (Map.Entry<String,Object> e : paramMap.entrySet()) {
                        b.queryParam(e.getKey(), e.getValue());
                    }
                    resp = restTemplate.getForEntity(b.build(true).toUri(), String.class);
                } else {
                    resp = restTemplate.postForEntity(j.getHttpUrl(), paramMap, String.class);
                }
                log.setStatus(resp.getStatusCode().is2xxSuccessful() ? "SUCCESS" : "FAIL");
                log.setMessage("HTTP " + resp.getStatusCode());
                log.setResultJson(resp.getBody());
            } else {
                throw new IllegalArgumentException("Unknown invokeType: " + type);
            }

        } catch (Exception ex) {
            log.setStatus("FAIL");
            log.setMessage(ex.getMessage());
            log.setResultJson(stackToJson(ex));
        } finally {
            log.setEndTime(System.currentTimeMillis());
            logMapper.insert(log);
        }
    }

    private Method findMethod(Class<?> clazz, String methodName, Object[] args) throws NoSuchMethodException {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) continue;
            Class<?>[] paramTypes = method.getParameterTypes();
            if ((args == null && paramTypes.length == 0) ||
                    (args != null && paramTypes.length == args.length)) {
                return method;
            }
        }
        throw new NoSuchMethodException(clazz.getName() + "." + methodName + "(...)");
    }

    private Map<String, Object> convertToParamMap(Object[] args) {
        if (args == null || args.length == 0) return Collections.emptyMap();
        if (args.length == 1 && args[0] instanceof Map) {
            return (Map<String, Object>) args[0];
        }
        if (args.length % 2 != 0) throw new IllegalArgumentException("参数数组必须是 key-value 对，长度为偶数");
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put(String.valueOf(args[i]), args[i + 1]);
        }
        return map;
    }



    private String stackToJson(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("stack", sw.toString());
            return om.writeValueAsString(m);
        } catch (Exception ex) {
            return null;
        }
    }
}
