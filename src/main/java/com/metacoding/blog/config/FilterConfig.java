package com.metacoding.blog.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.metacoding.blog.filter.JwtAuthFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilter() {
        FilterRegistrationBean<JwtAuthFilter> bean = new FilterRegistrationBean<>(new JwtAuthFilter());
        bean.addUrlPatterns("/boards", "/boards/*"); // 게시판 URL만 필터를 거친다 — /join·/login은 공개
        return bean;
    }
}
