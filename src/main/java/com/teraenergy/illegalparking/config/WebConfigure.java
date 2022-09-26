package com.teraenergy.illegalparking.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@Configuration
public class WebConfigure implements WebMvcConfigurer {

    @Value("${file.resourceUri}")
    String resourceUri;

    @Value("${file.resourcePath}")
    String resourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("web configure register [addResourceHandlers] : {} to {}", resourcePath, resourceUri);
        registry.addResourceHandler(resourceUri).addResourceLocations("file:///" + resourcePath);
    }

}
