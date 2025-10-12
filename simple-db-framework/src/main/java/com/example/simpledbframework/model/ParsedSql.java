package com.example.simpledbframework.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParsedSql {
    private String parsedSql;          // 替换后的 SQL
    private List<String> paramOrder;   // 参数顺序

    // getter / setter / 构造函数 ...

}
