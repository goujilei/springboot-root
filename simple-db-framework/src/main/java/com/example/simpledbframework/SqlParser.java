package com.example.simpledbframework;

import com.example.simpledbframework.model.ParsedSql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser {

    private static final Pattern PARAM_PATTERN = Pattern.compile("#\\{(\\w+)}");

    public static ParsedSql parse(String originalSql) {
        List<String> paramList = new ArrayList<>();
        Matcher matcher = PARAM_PATTERN.matcher(originalSql);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String paramName = matcher.group(1); // 提取 #{xxx} 中的 xxx
            paramList.add(paramName);
            matcher.appendReplacement(sb, "?");  // 替换为 ?
        }

        matcher.appendTail(sb); // 拼接剩余部分

        ParsedSql parsedSql = new ParsedSql();
        parsedSql.setParsedSql(sb.toString());
        parsedSql.setParamOrder(paramList);

        return parsedSql;
    }
}

