package com.login.pratice.configuration;

import com.login.pratice.service.UserService;
import com.login.pratice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    /**
     * 참고하면 좋을 자료
     * https://velog.io/@hellonayeon/spring-boot-jwt-expire-exception
     */

    /**
     * jwt는 매번 인증을 해야되서 OncePerRequestFilter를 상속
     */

    /**
     * 권한을 부여하는 층
     * doFilterInternal method는 문이라고 생각하면 됨 인증으로 막혀있는 문을 인증을 통해 통과하면 문을 열어서 다음단계로 통과한다고 생각
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // token 안보내면 Block
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("authentication을 잘못보냈습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // authorization에서 Token에서 꺼내기
        String token = authorization.split(" ")[1];

        // Token Expired되었는지 여부
        if (JwtUtil.isExpired(token, secretKey)) {
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        
        // userName에서 token 꺼내기
        String userName = JwtUtil.getUsername(token, secretKey);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
                                                                        // SimpleGrantedAuthority는 UserDetail interface에서 "USER_ROLE"같이 DB에서 지정을 해놨으면 꺼내서 집어넣어서 사용

        // Detail을 넣어줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // request넣어서 detail build
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // authenticationToken을 set해서
        filterChain.doFilter(request, response); // filterChain에다가 request를 넘겨주면 request 안쪽에 인증이 되었다고 인증 도장이 찍힘 그래서 다음으로 넘어갔을때 통과
    }

}
