package com.ecnu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护（WebSocket需要）
                .csrf().disable()

                // 配置请求授权
                .authorizeRequests()
                .antMatchers(
                        "/chat/**",          // WebSocket端点
                        "/favicon.ico",      // 图标
                        "/error",            // 错误页面
                        "/api/**"
                ).permitAll()           // 允许匿名访问
                .anyRequest().authenticated()  // 其他请求需要认证
                .and()

                // 关闭HTTP Basic认证（除非需要）
                .httpBasic().disable();
    }

}

