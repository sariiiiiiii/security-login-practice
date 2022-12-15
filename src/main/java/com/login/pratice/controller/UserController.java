package com.login.pratice.controller;

import com.login.pratice.domain.dto.UserJoinRequestDto;
import com.login.pratice.domain.dto.UserLoginRequest;
import com.login.pratice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * spring-security library 추가하게 되면
     * 어떠한 mapping 경로 호출시 401 Unauthorized 발생
     * X-Frame-Options: DENY (권한 없음)
     */

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequestDto dto) {
        userService.join(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok().body("회원가입이 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
        return ResponseEntity.ok().body(userService.login(dto.getUserName(), dto.getPassword()));
    }

}
