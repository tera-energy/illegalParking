package com.teraenergy.illegalparking.local;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RunWith(SpringRunner.class)
public class SqlCreate {

    private String createTable(String tableName, Map<String, String> columns) {
        String result = "CREATE TABLE " + tableName + " ( \n";
        Set<String> keys = columns.keySet();

        int size = keys.size();
        int count = 1;
        for( String key : keys ) {
            String name = key;
            String dataType = columns.get(key);
            if (count != size) {
                result += name + " " +dataType + ",\n";
            } else {
                result += name + " " +dataType + "\n";
            }
            count ++;
        }

        result += ") ENGINE=InnoDB CHARSET=utf8;";
        return result;
    }

    @Test
    public void lawDongSQL(){
        // key = Column Name / value = Data Type
        Map<String, String> columns = Maps.newHashMap();

        columns.put("seq", "INT NOT NULL AUTH_INCREMENT");
        columns.put("id", "VARCHAR(20)");
        columns.put("id", "VARCHAR(100)");

        String query = createTable("sample", columns);
        System.out.println(query);
    }

    @Test
    public void functionSQL(){
        String query = "";

        System.out.println(query);
    }
}
