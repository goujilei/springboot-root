package com.example.simpledbframework;


import com.example.simpledbframework.model.ParsedSql;

import java.util.HashMap;
import java.util.Map;

public class SqlRegistry {

    // 内部存储结构
    private static final Map<String, ParsedSql> sqlMap = new HashMap<>();

    // 注册 SQL
    public static void register(String sqlId, ParsedSql parsedSql) {
        sqlMap.put(sqlId, parsedSql);
    }

    // 获取 SQL
    public static ParsedSql getParsedSql(String sqlId) {
        ParsedSql parsedSql = sqlMap.get(sqlId);
        if (parsedSql == null) {
            throw new RuntimeException("SQL ID 未找到：" + sqlId);
        }
        return parsedSql;
    }

    // 清空（用于测试或重新加载）
    public static void clear() {
        sqlMap.clear();
    }
}
