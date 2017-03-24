package com.everteam.storage;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.everteam.storage.converters.FileIdConverter;
import com.everteam.storage.converters.StringToOffsetDateTimeConverter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Entry point for Extractor Application.
 *
 */
@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
public class StorageApplication extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FileIdConverter());
        registry.addConverter(new StringToOffsetDateTimeConverter());
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StorageApplication.class, args);
    }

    // To allow multipart requests with PUT methods. By default only POST
    // methods are permits.
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver() {
            @Override
            public boolean isMultipart(HttpServletRequest request) {
                String method = request.getMethod().toLowerCase();
                // By default, only POST is allowed. Since this is an 'update'
                // we should accept PUT.
                if (!Arrays.asList("put", "post").contains(method)) {
                    return false;
                }
                String contentType = request.getContentType();
                return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
            }
        };
    }
}