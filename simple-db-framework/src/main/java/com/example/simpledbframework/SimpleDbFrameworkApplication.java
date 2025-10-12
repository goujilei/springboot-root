package com.example.simpledbframework;

import com.example.simpledbframework.model.ParsedSql;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleDbFrameworkApplication {

    public static void main(String[] args) {
        //SpringApplication.run(SimpleDbFrameworkApplication.class, args);
        /*Map<String, String> sqls = XmlSqlLoader.loadSqlMap("sql/sql-001.xml");

        for (Map.Entry<String, String> entry : sqls.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }*/

        String sql = "SELECT * FROM user WHERE id = #{id} AND status = #{status}";

        ParsedSql parsed = SqlParser.parse(sql);

        System.out.println("SQL: " + parsed.getParsedSql());
        System.out.println("参数顺序: " + parsed.getParamOrder());
    }

}
