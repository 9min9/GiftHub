package com.gifthub.global.controller;

import com.gifthub.config.jwt.JwtContext;
import com.gifthub.user.UserJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContextController {

    private final UserJwtTokenProvider jwtTokenProvider;

    @PostMapping("/jwt/context/init")
    public ResponseEntity<Object> contextInit(@RequestHeader HttpHeaders headers) {
        try {
            System.out.println("init header");
            System.out.println(headers);
            String token = headers.get("Authorization").get(0);

            String name = jwtTokenProvider.getUsernameFromToken(token);
            System.out.println(name);

            JwtContext.setJwtToken(token);

            System.out.println("init Context Token");
            System.out.println(JwtContext.getJwtToken());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(Collections.singletonMap("status", "ok"));
    }



}
