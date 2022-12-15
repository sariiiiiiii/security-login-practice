package com.login.pratice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class SecurityConfig {

    /**
     * spring-security library 추가 후 application을 실행하면 Using generated security password: 뭐시깽이 하면서 출력되는것은 spring-security가 정상적으로 실행되었다는 뜻
     * SecurityConfig와 BCrypthPassword 클래스는 나눠줘야 된다 (순환참조 방지)
     */

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .httpBasic().disable() // UI쪽으로 들어오는 것을 disable()
//                .csrf().disable() // cross site 기능
//                .cors().and()// cross site에서 도메인이 다를 때 허용
//                .authorizeRequests()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
//                .and()
////                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthen)
//
//                .build();
//    }

}
