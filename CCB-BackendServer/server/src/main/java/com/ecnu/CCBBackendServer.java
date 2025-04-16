package com.ecnu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@EnableCaching
public class CCBBackendServer {
    public static void main(String[] args) {
        SpringApplication.run(CCBBackendServer.class, args);
        log.info("server started");
    }
}
