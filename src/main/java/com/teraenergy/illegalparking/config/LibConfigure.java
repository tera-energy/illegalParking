package com.teraenergy.illegalparking.config;

import com.teraenergy.illegalparking.lib.strategy.page.PageStrategy;
import com.teraenergy.illegalparking.lib.strategy.page.concrete.DefaultPageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@Configuration
public class LibConfigure {

    @Bean
    public PageStrategy pageStrategy() {
        log.info(" strategy configure register [PageStrategy - DefaultPageStrategy] ");
        return new DefaultPageStrategy();
    }
    
}
