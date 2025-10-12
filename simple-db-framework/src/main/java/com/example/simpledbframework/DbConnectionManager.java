package com.example.simpledbframework;

import com.example.simpledbframework.model.DbConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionManager {
    private static HikariDataSource dataSource;

    public static void init(DbConfig config) {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
