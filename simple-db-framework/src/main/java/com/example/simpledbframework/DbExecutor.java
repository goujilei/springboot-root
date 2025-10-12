package com.example.simpledbframework;

import com.example.simpledbframework.model.ParsedSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class DbExecutor {

    private static final Logger log = LoggerFactory.getLogger(DbExecutor.class);

    // 执行 SELECT
    public List<Map<String, Object>> select(String sqlId, Map<String, Object> params) {
        ParsedSql parsedSql = SqlRegistry.getParsedSql(sqlId);
        String realSql = parsedSql.getParsedSql();
        List<String> paramOrder = parsedSql.getParamOrder();

        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(realSql)) {

            // 绑定参数
            for (int i = 0; i < paramOrder.size(); i++) {
                Object value = params.get(paramOrder.get(i));
                ps.setObject(i + 1, value);
            }

            // 执行
            ResultSet rs = ps.executeQuery();
            return mapResultSet(rs);

        } catch (SQLException e) {
            throw new RuntimeException("执行 SELECT 出错：" + sqlId, e);
        }
    }

    // 执行 UPDATE / INSERT / DELETE
    public int update(String sqlId, Map<String, Object> params) {
        ParsedSql parsedSql = SqlRegistry.getParsedSql(sqlId);
        String realSql = parsedSql.getParsedSql();
        List<String> paramOrder = parsedSql.getParamOrder();

        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(realSql)) {

            // 绑定参数
            for (int i = 0; i < paramOrder.size(); i++) {
                Object value = params.get(paramOrder.get(i));
                ps.setObject(i + 1, value);
            }

            // 执行
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("执行更新出错：" + sqlId, e);
        }
    }

    // 封装 ResultSet → List<Map<String, Object>>
    private List<Map<String, Object>> mapResultSet(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            result.add(row);
        }

        return result;
    }
}
