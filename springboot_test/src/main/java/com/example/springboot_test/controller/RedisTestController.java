package com.example.springboot_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 写入 key-value
    @GetMapping("/set/{key}/{val}")
    public String set(@PathVariable String key, @PathVariable String val) {
        redisTemplate.opsForValue().set(key, val);
        return "成功写入: " + key + " = " + val;
    }

    // 读取 key 对应的值
    @GetMapping("/get")
    public String get(@RequestParam String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? "读取成功: " + key + " = " + value : "未找到 key: " + key;
    }

    // 删除 key
    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        Boolean result = redisTemplate.delete(key);
        return Boolean.TRUE.equals(result) ? "已删除 key: " + key : "key 不存在: " + key;
    }
}
