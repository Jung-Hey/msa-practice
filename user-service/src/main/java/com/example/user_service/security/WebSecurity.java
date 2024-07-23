package com.example.user_service.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    /**
     * 권한 정의
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Cross site Request forgery로 사이즈간 위조 요청, 즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것
        http.csrf().disable();
        // /user/로 시작하면 다 허용
        http.authorizeRequests().antMatchers("/users/**").permitAll();
        // h2 콘솔 오류 방지
        http.headers().frameOptions().disable();
    }
}
