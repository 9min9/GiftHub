package com.gifthub.user.controller;

import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @GetMapping("/points")
    public Long getPoint(@RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        return userService.getUserById(userId).getPoint();
    }

}
