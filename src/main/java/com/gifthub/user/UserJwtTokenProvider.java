package com.gifthub.user;

import com.gifthub.user.dto.TokenInfo;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserJwtTokenProvider {
    private final Long expiredTime = 1000 * 60L * 60L * 1L; // 유효시간 1시간
    private final Key key;
    private final CustomUserDetailsService userDetailsService;

    public UserJwtTokenProvider(@Value("${providerKey}") String secretKey, CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateTokenInfo(UserDto userDto) {
        Date now = new Date();
        String refreshToken = generateRefreshToken(now);
        String accessToken = generateAccessToken(userDto, now);

        return TokenInfo.builder().grantType("").accessToken(accessToken).refreshToken(refreshToken).userRole(userDto.getUserType()).build();
    }

    private String generateRefreshToken(Date now) {
        return Jwts.builder().setExpiration(new Date(now.getTime() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private String generateAccessToken(UserDto userDto, Date now) {
        return Jwts.builder().setSubject(userDto.getEmail())
                .setHeader(createHeader()).setClaims(createClaims(userDto)) // 클레임, 토큰에 포함될 정보
                .setExpiration(new Date(now.getTime() + expiredTime))        // 만료일
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(UserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDto.getId());
        claims.put("username", userDto.getEmail()); // username
        claims.put("accountType", userDto.getLoginType());
        claims.put("roles", userDto.getUserType());

        return claims;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    /**
     * User
     */
    public String getUsernameFromToken(String token) {
        return (String) getClaims(token).get("username");
    }

    public String getAccountTypeFromToken(String token) {
        return (String) getClaims(token).get("accountType");
    }

    public int getRoleFromToken(String token) {
        return (int) getClaims(token).get("roles");
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return ((Integer) claims.get("userId")).longValue();
    }

    public String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null) {
            log.info("Authorization header value: " + authorization);
            System.out.println("Authorization header value: " + authorization);
        } else {
            log.warn("Authorization header not found");
        }

        return authorization;
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
