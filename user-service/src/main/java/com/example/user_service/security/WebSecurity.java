package com.example.user_service.security;

import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;
    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    /**
     * 권한 정의
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Cross site Request forgery로 사이즈간 위조 요청, 즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것
        http.csrf().disable();
        //http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**") // 제약조건 1.  모든요청 통과 X
                .hasIpAddress("192.168.219.201") // 제약조건 2. 통과시킬 IP
                .and()
                .addFilter(getAuthenticationFilter());// 제약조건 3. 이 필터 통과해야 권한 부여함

        // h2 콘솔 오류 방지
        http.headers().frameOptions().disable();
    }

    //기본은 Filter 반환인데 우리가 만든 AuthenticationFilter(필터 상속받아 가능) 반환
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), userService, env);

        return authenticationFilter;
    }

    @Override
    //인증 정의
    // select pwd from users where email=?
    // db_pwd(encrypted) == input_pwd(encrypted)
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
