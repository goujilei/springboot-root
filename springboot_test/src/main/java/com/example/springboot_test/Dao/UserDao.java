package com.example.springboot_test.Dao;

import com.example.springboot_test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getUserCount() {
        try {
            System.out.println(jdbcTemplate.queryForObject("SELECT user_name FROM t_user", String.class));
        } catch (Exception e) {
            System.out.println("查询user表时发生异常:"+e.getMessage());
        }
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_user", Integer.class);
    }
}
