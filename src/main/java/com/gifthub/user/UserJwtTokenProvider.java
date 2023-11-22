package com.gifthub.user;

import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//인증 완료후 토큰 생성해서 주는 클래스	2
@Component
@RequiredArgsConstructor
public class UserJwtTokenProvider {
	//토큰 유효시간
	private final Long expiredTime = 1000 * 60L * 60L * 1L; // 유효시간 1시간
	@Value("${providerKey}")
	private String providerKey;
	Key key = Keys.hmacShaKeyFor("7JWU7Zi47ZmU7ZmU7Zi47JWU7JWU7JWU7Zi47ZmU7JWU7Zi47ZmU7ZW067O8656Y7JWU7Zi47ZmU67O8656Y7ZW07JWU7Zi47ZmU".getBytes(StandardCharsets.UTF_8));

	private final CustomUserDetailsService userDetailsService;

	public String generateJwtToken(UserDto userDto) {
		Date now = new Date();
		return Jwts.builder().setSubject(userDto.getEmail())
				.setHeader(createHeader()).setClaims(createClaims(userDto)) // 클레임, 토큰에 포함될 정보
				.setExpiration(new Date(now.getTime() + expiredTime)) 		// 만료일
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

	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	/** User */
	public String getUsernameFromToken(String token) {
		return (String) getClaims(token).get("email");
	}

	public String getAccountTypeFromToken(String token) {
		return (String) getClaims(token).get("accountType");
	}

	public int getRoleFromToken(String token) {
		return (int) getClaims(token).get("roles");
	}

	/** */
	public String resolveToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");

		if (authorization != null) {
			System.out.println("Authorization header value: " + authorization);
		} else {
			System.out.println("Authorization header not found");
		}

		System.out.println(authorization);
		return authorization;
	}

	public boolean validateToken(String token) {
		try {
			System.out.println("validate Token!!!");
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			return !claims.getExpiration().before(new Date());
		} catch (Exception e) {
			System.out.println("validate Token Error!!!");
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsernameFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

}
