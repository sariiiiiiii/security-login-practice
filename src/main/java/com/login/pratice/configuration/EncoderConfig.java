package com.login.pratice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncoderConfig {

    /**
     * DB에 개인정보를 평문으로 넣을 수 없으니 security의 BCryptPasswordEncoder 사용
     * 또, SecurityConfig와 BCrypthPassword 클래스는 나눠줘야 된다 (순환참조 방지)
     */

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
