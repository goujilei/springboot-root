package com.example.simpledbframework;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlLoader {

    public static Map<String, String> loadSqlMap(InputStream is) {
        Map<String, String> sqlMap = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            NodeList sqlNodes = doc.getElementsByTagName("*");

            for (int i = 0; i < sqlNodes.getLength(); i++) {
                Element element = (Element) sqlNodes.item(i);
                String id = element.getAttribute("id");
                String sql = element.getTextContent().trim();
                sqlMap.put(id, sql);
            }

        } catch (Exception e) {
            throw new RuntimeException("解析 SQL XML 失败", e);
        }

        return sqlMap;
    }

}
