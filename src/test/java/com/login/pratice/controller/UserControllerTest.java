package com.login.pratice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.pratice.domain.dto.UserJoinRequestDto;
import com.login.pratice.domain.dto.UserLoginRequest;
import com.login.pratice.exception.AppException;
import com.login.pratice.exception.ErrorCode;
import com.login.pratice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.login.pratice.exception.ErrorCode.USERNAME_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * with(csrf()) - spring security test library
     * spring security의 annotation인 @EnableWebSecurity에는 기본적으로 CSRF 공격을 방지하는 기능을 지원한다
     * IDE에서 test할 때 403 오류가 나지 않게 파라미터에 csrf를 추가해주는 with(csrf))를 추가해주자
     * https://kang-james.tistory.com/entry/%EB%B3%B4%EC%95%88%EC%9D%B8%EC%A6%9D-CSRF-%EB%A1%9C-%EC%9D%B8%ED%95%B4%EC%84%9C-403%EC%97%90%EB%9F%AC%EA%B0%80-%EB%B0%9C%EC%83%9D%ED%96%88%EC%9D%84-%EB%95%8C
     */

    /**
     * @WithAnonymousUser = 익명의 유저 생성
     * @WithMockUser = 사용자 이름, 패스워드, 권한으로 UserDetails를 생성한 후 보안 콘텍스트를 로드. 값을 지정하지 않으면 default 값으로 지정
     * https://shinsunyoung.tistory.com/94
     */

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequestDto("sari", "sari"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - userName 중복")
    @WithMockUser
    void join_fail() throws Exception {

        when(userService.join(any(), any()))
                .thenThrow(new RuntimeException("해당 userId가 중복됩니다."));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequestDto("sari", "sari"))))
                .andDo(print())
                .andExpect(status().isConflict()); // 409 클라이언트의 요청이 서버의 상태와 충돌
    }

    /**
     * 로그인 성공 -> SUCCESS
     * 로그인 실패 - userName 없음 -> NOT FOUND
     * 로그인 실패 - password 틀림 - > UNAUTHORIZED
     */

    @Test
    @DisplayName("로그인 성공 -> success")
    @WithMockUser
    void login() throws Exception {

        when(userService.login(any(), any()))
                .thenReturn("token"); // 로그인 성공은 token을 발행하는데 token을 mock으로 처리

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("sari", "sari"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - userName 없음")
    @WithMockUser
    void login_fail1() throws Exception {
        when(userService.login(any(), any()))
                .thenThrow(new AppException(USERNAME_NOT_FOUND, ""));


        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("sari12", "sari"))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password 틀림")
    @WithMockUser
    void login_fail2() throws Exception {
        when(userService.login(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("sari", "sari"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}