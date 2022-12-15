package com.login.pratice.configuration;

import com.login.pratice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserService userService;

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * @EnableWebSecurity 적용하면 모든 api에 인증이 필요하다라고 spring-security가 default로 설정됨
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // UI쪽으로 들어오는 것을 disable()
                .csrf().disable() // cross site 기능
                .cors().and() // cross site에서 도메인이 다를 때 허용
                .authorizeRequests() // request를 authorize 하겠다
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll() // login과 join은 인증 없이 가능
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated() // permitAll을 제외한 인증 필요로 막아놓음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();

        /**
         * 받은 token을 풀려면 secretKey 필요
         * JwtFilter 계층을 UsernamePasswordAuthenticationFilter.class 앞에다가 위치
         */

    }

}
