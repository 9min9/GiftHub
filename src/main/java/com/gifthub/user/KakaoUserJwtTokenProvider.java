package com.gifthub.user;

import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class KakaoUserJwtTokenProvider {
    private final Long expiredTime = 1000 * 60L * 60L * 1L; // 유효시간 1시간
    Key key = Keys.hmacShaKeyFor("7Lm07Lm07Jik7JWU7Zi47ZmU7Lm07Lm07Jik7JWU7Zi47ZmU7Lm07Lm07Jik7JWU7Zi47ZWY7ZmU7JWE7Lm07JWE7Lm07Lm07Jik7Jik7JWU7Zi47Zi47ZWY7JWE7JWECg==".getBytes(StandardCharsets.UTF_8));
    private final CustomUserDetailsService userDetailsService;

//  public String generateJwtTokenKakao(KakaoUserDto kakaoUserDto){
//	  Date now = new Date();
//	  return Jwts.builder().setSubject(kakaoUserDto.getEmail()) // 보통 username
//			  .setHeader(createHeader()).setClaims(createClaims(kakaoUserDto)) // 클레임, 토큰에 포함될 정보
//			  .setExpiration(new Date(now.getTime() + expiredTime)) // 만료일
//			  .signWith(key, SignatureAlgorithm.HS256).compact();
//  }

    public String generateKakaoJwtToken(KakaoUserDto kakaoUserDto) {
        Date now = new Date();
        return Jwts.builder().setSubject(kakaoUserDto.getEmail()) // 보통 username
                .setHeader(createHeader()).setClaims(createClaims(kakaoUserDto)) // 클레임, 토큰에 포함될 정보
                .setExpiration(new Date(now.getTime() + expiredTime)) // 만료일
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256"); // 해시 256 사용하여 암호화
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(KakaoUserDto kakaoUserDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", kakaoUserDto.getEmail());
        claims.put("accountType", "Kakao");
        //claims.put("roles", member.getType()); // 인가정보
        return claims;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return (String) getClaims(token).get("username");
    }

//	public int getRoleFromToken(String token) {
//		return (int) getClaims(token).get("roles");
//	}

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
