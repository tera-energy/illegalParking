package com.teraenergy.illegalparking.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.sql.DataSource;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description : hikari datasource  +   ( mybatis + jpa )
 */
@Slf4j
@Lazy
@RequiredArgsConstructor
@MapperScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.teraenergy.illegalparking.model.mapper"}
)
//@Configuration
public class HikariDatasourceConfigure {

    private final ApplicationContext context;

    @Value("${mybatis.mapper-locations}")
    String mapperPath;

    /**
     * application.yml 에서 spring.datasource.hikari 를 지정하여 DB 를 한개 더 연결할때 사용 ( 또는 hikari 설정을 이용할때 사용 )
     * 주의 : 기존의 datasource 와 충돌이 되지 않도록 설정이 중요.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource hikariDatasource(){
        log.info("configure mybatisDatasource ");
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("hikariDatasource") DataSource dataSource) throws Exception {
        return _sqlSessionFactory(dataSource);
    }

    public SqlSessionFactory _sqlSessionFactory(DataSource dataSource) throws Exception {
        log.info("configure sqlSessionFactory for mybatisDatasource ");
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        // 실제 쿼리가 들어갈 xml 패키지 경로
        sessionFactory.setMapperLocations(context.getResources(mapperPath));

        // Value Object를 선언해 놓은 package 경로
        // Mapper의 result, parameterType의 패키지 경로를 클래스만 작성 할 수 있도록 도와줌.
        sessionFactory.setTypeAliasesPackage("com.teraenergy.illegalparking.model.entity");

        // enum type 을 mybatis 에서 사용하기위해 설정
//        sessionFactory.setTypeHandlers(new TypeHandler[]{
//                new IllegalType.TypeHandler()
//        });

        return sessionFactory.getObject();
    }


    // Mybatis Template
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        sqlSessionTemplate.getConfiguration().setMapUnderscoreToCamelCase(true);
//        sqlSessionTemplate.getConfiguration().setUseGeneratedKeys(true);
        return sqlSessionTemplate;
    }

//    @Bean
//    public DataSourceTransactionManager transactionManager(@Qualifier("hikariDatasource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
