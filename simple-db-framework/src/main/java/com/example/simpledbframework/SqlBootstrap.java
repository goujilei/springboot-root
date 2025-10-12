package com.example.simpledbframework;

import com.example.simpledbframework.model.DbConfig;
import com.example.simpledbframework.model.ParsedSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

@Component
public class SqlBootstrap {

    @Autowired
    private DbConfig dbConfig;

    private static final Logger log = LoggerFactory.getLogger(SqlBootstrap.class);

    public void init() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // classpath* 能扫描 jar 包和本地目录
            Resource[] resources = resolver.getResources("classpath*:sql/*.xml");

            for (Resource resource : resources) {
                log.info("加载 SQL 文件：{}", resource.getFilename());

                // 使用 InputStream 读取 XML
                try (InputStream is = resource.getInputStream()) {

                    // 假设 XmlSqlLoader 支持传入字符串内容
                    Map<String, String> rawSqlMap = XmlSqlLoader.loadSqlMap(is);
                    for (Map.Entry<String, String> entry : rawSqlMap.entrySet()) {
                        ParsedSql parsed = SqlParser.parse(entry.getValue());
                        SqlRegistry.register(entry.getKey(), parsed);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("自动加载 SQL 配置失败", e);
        }

        DbConnectionManager.init(dbConfig);
    }
}

