package com.login.pratice.service;

import com.login.pratice.domain.User;
import com.login.pratice.exception.AppException;
import com.login.pratice.exception.ErrorCode;
import com.login.pratice.repository.UserRepository;
import com.login.pratice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    /**
     * @Value = jwt.token.secret
     * 실제로는 Environment variables에 넣음
     */

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String key;

    private Long expireTimeMs = 1000 * 60 * 60L;

    public String join(String userName, String password) {

        // userName 중복 check
        userRepository.findByUsername(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 있습니다.");
                });

        // 저장
        userRepository.save(User.builder()
                .username(userName)
                .password(encoder.encode(password))
                .build());
        return "SUCCESS";
    }

    public String login(String userName, String password) {
        // userName 없음
        User selectedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        // password 틀림
        if (!encoder.matches(password, selectedUser.getPassword())) { // 입력받은 string먼저, 조회 후 encoding 된 string이 2번째 인자로
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력 했습니다.");
        }

        // exception 안났으면 토큰 발행
        String token = JwtUtil.createJwt(selectedUser.getUsername(), key, expireTimeMs);
        log.info("token = {}", token);
        return token;
    }
}
