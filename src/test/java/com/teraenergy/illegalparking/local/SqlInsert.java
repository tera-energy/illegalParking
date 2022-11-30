package com.teraenergy.illegalparking.local;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RunWith(SpringRunner.class)
public class SqlInsert {

    public String getData(Object obj) {
        String value ="";
        switch (obj.getClass().getName()) {
            case "String":
                value = "'" + value + "'";
                break;
            case "int":
                value = value + "";
                break;
            case "boolean":
                value = Boolean.valueOf((boolean) obj).toString();
                break;
        }
        return value;

    }

    public String insert(String table, List<String> columns, List<Object> datas) {

        String query = "insert into ";
        query += table + "(";

        int cnt = 1;
        int size = columns.size();
        for (String column : columns) {
            if (cnt != size) {
                query += column + ",";
            } else {
                query += column;
            }
            cnt++;
        }

        query += ") values (";

        cnt = 1;
        size = datas.size();
        for (Object data : datas) {
            if (cnt != size) {
                query += getData(data) + ",";
            } else {
                query += getData(data) + "";
            }
            cnt++;
        }

        query += ");";
        return query;
    }

    @Test
    public void lawDongSQL() {
        System.out.println(insert("law_dong", null, null));
    }

    @Test
    public void parkingSQL() {
        // TODO : Excel 데이터 읽기

        // TODO : 데이터 저장
    }

    @Test
    public void generateUUID() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        String abstractUUID = uuid.substring(19).toUpperCase(Locale.ROOT);
        System.out.println(abstractUUID);
    }

}
