package com.teraenergy.illegalparking.dialect;

import org.hibernate.dialect.MariaDB103Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */
public class MyMariaDB103Dialect extends MariaDB103Dialect {

    /**
     * Mariadb 의 GEOMETRY 함수를 추가한 Dialect
     *
     * 적용 방법
     *  - application.yml 파일 수정 -
     *  spring:
     *      ...
     *      jpa:
     *        database-platform: org.hibernate.dialect.MariaDB103Dialect  <<< 삭제
     *        database-platform: com.teraenergy.illegalparking.dialect.MyMariaDB103Dialect <<< 추가
     */
    public MyMariaDB103Dialect() {
        super();
        this.registerFunction( "ASTEXT", new StandardSQLFunction( "ASTEXT", StandardBasicTypes.CHARACTER) );
        this.registerFunction( "POLYGONFROMTEXT", new StandardSQLFunction( "POLYGONFROMTEXT", StandardBasicTypes.CHARACTER) );
    }

}
