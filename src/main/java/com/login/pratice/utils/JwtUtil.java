package com.login.pratice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {


    public static String getUsername(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userName", String.class);
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userName").toString();
    }

    public static boolean isExpired(String token, String secretKey) {

        /**
         * token이 expired 된게 지금 (new Date) 보다 전인지 판단
         */

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration() // 여기까지 Date형식
                .before(new Date()); // 현재날짜랑 비교
    }

    public static String createJwt(String userName, String secretKey, long expireTimeMs) {
        /**
         * 정보같은 것을 넣고 싶으면 Claims에다가 넣으면 됨
         * 현재 userName이 claims 안으로 들어가게 됨
         */
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("userName", userName); // 받아온 username 저장

        return Jwts.builder()
                .setClaims(claims) // claims 지정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 만든 날짜 (token 발행날짜) 즉, 오늘날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 끝나는 날짜 (token 만기 날짜)
                .signWith(SignatureAlgorithm.HS256, secretKey) // key를 가지고 시그니쳐 알고리즘(HS256) 사인
                .compact();
    }


}
