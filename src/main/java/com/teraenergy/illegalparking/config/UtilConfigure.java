package com.teraenergy.illegalparking.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Date : 2022-09-10
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@Configuration
public class UtilConfigure {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public ObjectMapper setObjectMapper() {
        log.info(" util configure register [ObjectMapper] ");
        ObjectMapper objectMapper = new ObjectMapper();

        // deserialize 하는 동안 매핑 되지 않은것이 있어도 무시하고 진행
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // ObjectMapper 에서 datetime 변환할때 jsr310 적용을 위해 JavaTimeModule 을 등록해야 함
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    @Bean
    public JPAQueryFactory queryFactory() {
        log.info(" util configure register [JPAQueryFactory] ");
        return new JPAQueryFactory(entityManager);
    }


}
