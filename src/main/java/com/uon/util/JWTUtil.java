package com.uon.util;

import java.util.Date;

import javax.crypto.SecretKey;

import com.uon.user.dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
    @Value("${jwt.secret-key}")
    private String secretKeyPlain;

    private final long EXPIRATION_SECONDS = 60 * 60;	//1시간

    //application.properties에 등록된 변수
    public SecretKey getSecretKey() {
//    	System.out.println("응애 : " + secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // 토큰 생성
    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_SECONDS * 1000);

        return Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .claim("birth", user.getBirth())
                .expiration(expiration)				//만료 시간
                .signWith(getSecretKey())
                .compact();
    }

    // 토큰 유효성 검사
    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.debug("토큰 유효성 검증 오류 : {}",e.getMessage());
            return false;
        }
    }

    //토큰으로 부터 ID 조회
    public String getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String id = (String) claims.get("id");
        log.debug("claim id:{}",id);
        return id;
    }

    //토큰으로 부터 role 조회
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String role = (String) claims.get("role");
        log.debug("claim role:{}",role);
        return role;
    }

    //토큰으로 부터 birth 조회
    public String getbirthFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String birth = (String) claims.get("birth");
        log.debug("claim id:{}",birth);
        return birth;
    }
}


