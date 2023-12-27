package com.gifthub.user.controller;

import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @PostMapping("/login/verify")
    public ResponseEntity<Object> isLogin(@RequestHeader HttpHeaders headers) {
        String token = headers.get("Authorization").get(0);
        boolean isLogin = userJwtTokenProvider.validateToken(token);
        String role = userJwtTokenProvider.getRoleFromToken(token);
        Authentication authentication = userJwtTokenProvider.getAuthentication(token);

        if (isLogin) {
            return ResponseEntity.ok().body(Collections.singletonMap("role", role));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/myinfo")
    public ResponseEntity<Object> myInfo(@RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            UserDto findUser = userService.getUserById(userId);
            return ResponseEntity.ok().body(findUser);

        } catch (Exception e) {
            log.error("UserController | myInfo | " +e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/points")
    public Long getPoint(@RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        return userService.getUserById(userId).getPoint();
    }

}
